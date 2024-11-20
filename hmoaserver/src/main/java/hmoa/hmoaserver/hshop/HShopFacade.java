package hmoa.hmoaserver.hshop;

import hmoa.hmoaserver.common.PageSize;
import hmoa.hmoaserver.common.PageUtil;
import hmoa.hmoaserver.common.PagingDto;
import hmoa.hmoaserver.exception.Code;
import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.fcm.dto.FCMNotificationRequestDto;
import hmoa.hmoaserver.fcm.service.FCMNotificationService;
import hmoa.hmoaserver.fcm.service.constant.NotificationType;
import hmoa.hmoaserver.hshop.domain.*;
import hmoa.hmoaserver.hshop.dto.*;
import hmoa.hmoaserver.hshop.service.CartService;
import hmoa.hmoaserver.hshop.service.HbtiReviewService;
import hmoa.hmoaserver.hshop.service.NoteProductService;
import hmoa.hmoaserver.hshop.service.OrderService;
import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.member.service.MemberAddressService;
import hmoa.hmoaserver.member.service.MemberInfoService;
import hmoa.hmoaserver.member.service.MemberService;
import hmoa.hmoaserver.note.domain.Note;
import hmoa.hmoaserver.note.service.NoteService;
import hmoa.hmoaserver.photo.domain.HbtiPhoto;
import hmoa.hmoaserver.photo.dto.PhotoResponseDto;
import hmoa.hmoaserver.photo.service.HbtiPhotoService;
import hmoa.hmoaserver.photo.service.PhotoService;
import hmoa.hmoaserver.recommend.survey.domain.NoteRecommend;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class HShopFacade {

    private static final int SHIPPING_FEE = 3000;

    @Value("${default.order-description}")
    private String orderDescriptionImgUrl;

    @Getter
    private NoteOrderDescriptionResponseDto noteOrderDescriptionResponseDto;

    @PostConstruct
    public void init() {
        this.noteOrderDescriptionResponseDto = new NoteOrderDescriptionResponseDto(orderDescriptionImgUrl);
    }

    private final NoteProductService noteProductService;
    private final NoteService noteService;
    private final MemberService memberService;
    private final MemberInfoService memberInfoService;
    private final MemberAddressService memberAddressService;
    private final OrderService orderService;
    private final CartService cartService;
    private final HbtiReviewService hbtiReviewService;
    private final PhotoService photoService;
    private final HbtiPhotoService hbtiPhotoService;
    private final FCMNotificationService fcmNotificationService;

    // 상품 등록
    public void registerProduct(NoteProductSaveRequestDto dto) {
        Note note = noteService.findByTitle(dto.getNoteName());
        NoteProduct noteProduct = dto.toEntity(note);

        noteProductService.save(noteProduct);
    }

    // 상품 조회
    public List<NoteProductResponseDto> getNoteProducts(String token) {
        Member member = memberService.findByMember(token);

        List<NoteProduct> noteProducts = noteProductService.getAllNoteProducts();
        NoteRecommend recommendNotes = member.getNoteRecommend();
        if (recommendNotes == null) {
            throw new CustomException(null, Code.HBTI_NOT_SUBJECTED);
        }

        List<NoteProductResponseDto> result = new ArrayList<>();

        for (NoteProduct noteProduct : noteProducts) {
            boolean recommend = recommendNotes.getRecommendNotes().contains(noteProduct.getNote().getTitle());
            result.add(new NoteProductResponseDto(noteProduct, recommend));
        }

        return result;
    }

    // 구매할 향료 입력
    public NoteProductsResponseDto selectNoteProducts(String token, NoteProductSelectRequestDto dto) {
        Member member = memberService.findByMember(token);
        Optional<Cart> cart = cartService.findOneCartByMemberId(member.getId());
        NoteProductsResponseDto result = noteProductService.getNoteProducts(dto.getProductIds());

        // cart가 존재할 시
        if (cart.isPresent()) {
            cartService.updateCart(cart.get(), dto.getProductIds(), result.getTotalPrice());
            return result;
        }

        // cart 첫 저장
        cartService.save(dto.toCartEntity(member.getId(), result.getTotalPrice()));
        return result;
    }

    //향료 주문 요청
    public OrderResponseDto orderNotes(String token, NoteProductSelectRequestDto dto) {
        Member member = memberService.findByMember(token);

        NoteProductsResponseDto noteProducts = noteProductService.getNoteProducts(dto.getProductIds());
        String orderTitle = noteProducts.getNoteProducts().get(0).getProductName();
        if (dto.getProductIds().size() > 1) {
            String orderFormat = String.format(" 외 %d건", dto.getProductIds().size() - 1);
            orderTitle += orderFormat;
        }
        OrderEntity order = orderService.firstOrderSave(member, orderTitle, dto.getProductIds(), noteProducts.getTotalPrice());
        boolean isExistMemberInfo = memberInfoService.isExistMemberInfo(member.getId());
        boolean isExistMemberAddress = memberAddressService.isExistMemberAddress(member.getId());

        return new OrderResponseDto(order, isExistMemberInfo, isExistMemberAddress);
    }

    // 향료 주문 정보 조회
    public OrderInfoResponseDto getOrderInfos(String token, Long orderId) {
        Member member = memberService.findByMember(token);
        OrderEntity order = orderService.findById(orderId);

        memberService.checkAuthorization(member.getId(), order.getMemberId());

        NoteProductsResponseDto noteProducts = noteProductService.getNoteProducts(order.getProductIds());

        return new OrderInfoResponseDto(noteProducts, order.getTotalPrice(), SHIPPING_FEE);
    }

    // 결제 전 주문 상품 제거
    public OrderInfoResponseDto deleteProduct(String token, Long orderId, Long productId) {
        Member member = memberService.findByMember(token);
        OrderEntity order = orderService.findById(orderId);
        NoteProduct product = noteProductService.getNoteProduct(productId);

        memberService.checkAuthorization(member.getId(), order.getMemberId());
        orderService.deleteProduct(order, product);
        NoteProductsResponseDto noteProducts = noteProductService.getNoteProducts(order.getProductIds());
        String orderTitle = "";

        if (!noteProducts.getNoteProducts().isEmpty()) {
            orderTitle = noteProducts.getNoteProducts().get(0).getProductName();
            if (order.getProductIds().size() > 1) {
                String orderFormat = String.format(" 외 %d건", order.getProductIds().size());
                orderTitle += orderFormat;
            }
        }
        
        orderService.updateOrderTitle(order, orderTitle);

        return new OrderInfoResponseDto(noteProducts, order.getTotalPrice(), SHIPPING_FEE);
    }

    // 장바구니 조회
    public NoteProductsResponseDto getCartInfos(String token) {
        Member member = memberService.findByMember(token);

        Cart cart = cartService.findOneCartByMemberId(member.getId()).orElseThrow(() -> new CustomException(null, Code.CART_NOT_FOUND));

        return noteProductService.getNoteProducts(cart.getProductIds());
    }

    // order내역 지우기
    public void deleteOrders(String token) {
        Member member = memberService.findByMember(token);
        List<OrderEntity> orders = orderService.findByMemberId(member.getId());
        orderService.deleteOrders(orders);
    }

    // order 지우기 (id 로)
    public void deleteOrder(String token, Long orderId) {
        OrderEntity order = orderService.findById(orderId);
        orderService.deleteOrder(order);
    }

    // 후기 작성 가능한 주문 조회
    public List<OrderSelectResponseDto> getReviewableOrders(String token) {
        Member member = memberService.findByMember(token);
        List<OrderEntity> orders = orderService.findByMemberId(member.getId());
        List<OrderEntity> filteredOrders = orders.stream()
                .filter(order -> !hbtiReviewService.isPresentHbtiReviewByMember(order.getId(), member.getId()))
                .filter(order -> OrderStatus.getAllStatus().contains(order.getStatus()))
                .limit(PageSize.FIFTY_SIZE.getSize())
                .toList();
        return filteredOrders.stream().map(OrderSelectResponseDto::new).toList();
    }

    // 향비티아이 후기 저장
    public HbtiReviewResponseDto saveHbtiReview(String token, Long orderId, List<MultipartFile> files, HbtiReviewSaveRequestDto dto) {
        Member member = memberService.findByMember(token);
        OrderEntity order = orderService.findById(orderId);

        HbtiReview hbtiReview = hbtiReviewService.save(dto.toEntity(member.getId(), order.getId()));

        List<HbtiPhoto> photos = new ArrayList<>();

        if (files != null) {
            photoService.validateReviewPhotoCountExceeded(files.size());
            photos = hbtiReviewService.saveHbtiPhotos(hbtiReview, files);
        }

        return createReviewResponseDto(hbtiReview, order, member, photos);
    }

    // 후기 수정
    public HbtiReviewResponseDto modifyHbtiReview(String token, Long reviewId, List<MultipartFile> files, HbtiReviewModifyRequestDto dto) {
        Member member = memberService.findByMember(token);
        HbtiReview review = hbtiReviewService.getReview(reviewId);
        OrderEntity order = orderService.findById(review.getOrderId());

        validateOwner(member, review);

        List<HbtiPhoto> curPhoto = new ArrayList<>(review.getHbtiPhotos().stream()
                .filter(photo -> !dto.getDeleteReviewPhotoIds().contains(photo.getId()))
                .toList());

        hbtiPhotoService.deleteAll(dto.getDeleteReviewPhotoIds());
        hbtiReviewService.modifyHbtiReview(review, dto);

        if (files != null) {
            curPhoto.addAll(hbtiReviewService.saveHbtiPhotos(review, files));
        }

        return createReviewResponseDto(review, order, member, curPhoto);
    }

    // 후기 목록 조회
    public PagingDto<Object> getHbtiReviews(String token, int page) {
        Member member = memberService.findByMember(token);
        Page<HbtiReview> hbtiReviews = hbtiReviewService.getHbtiReviewsByPage(page);
        List<HbtiReviewResponseDto> res = createReviewResponseDtos(hbtiReviews, member);
        boolean isLastPage = PageUtil.isLastPage(hbtiReviews);

        return PagingDto.builder()
                .isLastPage(isLastPage)
                .data(res)
                .build();
    }

    // 후기 좋아요
    public void saveHbtiReviewHeart(String token, Long reviewId) {
        Member member = memberService.findByMember(token);
        HbtiReview review = hbtiReviewService.getReview(reviewId);

        hbtiReviewService.saveHeart(review.getId(), member.getId());
        hbtiReviewService.increaseHbtiHeartCount(review);
        fcmNotificationService.sendNotification(new FCMNotificationRequestDto(review.getMemberId(), member.getNickname(), member.getId(), NotificationType.HBTI_REVIEW_LIKE, reviewId));
    }

    // 후기 좋아요 취소
    public void deleteHbtiReviewHeart(String token, Long reviewId) {
        Member member = memberService.findByMember(token);
        HbtiReview review = hbtiReviewService.getReview(reviewId);

        hbtiReviewService.deleteHeart(review.getId(), member.getId());
        hbtiReviewService.decreaseHbtiHeartCount(review);
    }

    // 내가 작성한 후기 목록
    public PagingDto<Object> getMyReviews(String token, Long cursor) {
        Member member = memberService.findByMember(token);
        if (PageUtil.isFistCursor(cursor)) cursor = PageUtil.convertFirstCursor(cursor);
        Page<HbtiReview> reviews = hbtiReviewService.getHbtiReviewsByMemberAndCursor(member.getId(), cursor);
        List<HbtiReviewResponseDto> res = createReviewResponseDtos(reviews, member);
        boolean isLastPage = PageUtil.isLastPage(reviews);

        return PagingDto.builder()
                .isLastPage(isLastPage)
                .data(res)
                .build();
    }

    // 후기 삭제
    public void deleteReview(String token, Long reviewId) {
        Member member = memberService.findByMember(token);
        HbtiReview review = hbtiReviewService.getReview(reviewId);

        validateOwner(member, review);

        OrderEntity order = orderService.findById(review.getOrderId());
        List<HbtiReviewHeart> hearts = hbtiReviewService.getReviewHeartsByReviewId(reviewId);
        hbtiReviewService.deleteHbtiReviewHeart(hearts);
        hbtiReviewService.deleteHbtiReview(review);
    }

    // 후기 단 건 조회
    public HbtiReviewResponseDto getReview(String token, Long reviewId) {
        Member member = memberService.findByMember(token);
        HbtiReview review = hbtiReviewService.getReview(reviewId);

        boolean isWrited = review.getMemberId().equals(member.getId());
        boolean isLiked = hbtiReviewService.isPresentReviewHeart(reviewId, member.getId());
        Member author = memberService.findById(review.getMemberId()).get();
        OrderEntity order = orderService.findById(review.getOrderId());
        return new HbtiReviewResponseDto(review, order.getTitle(), author, isWrited, isLiked);
    }

    private void validateOwner(Member member, HbtiReview review) {
        if (!member.getId().equals(review.getMemberId())) {
            throw new CustomException(null, Code.FORBIDDEN_AUTHORIZATION);
        }
    }

    private HbtiReviewResponseDto createReviewResponseDto(HbtiReview review, OrderEntity order, Member member, List<HbtiPhoto> curPhoto) {
        HbtiReviewResponseDto res = new HbtiReviewResponseDto(review, order.getTitle(), member, true, false);
        res.setImagesCount(curPhoto.size());
        res.setHbtiPhotos(curPhoto.stream().map(PhotoResponseDto::new).toList());
        return res;
    }

    public List<HbtiReviewResponseDto> createReviewResponseDtos(Page<HbtiReview> reviews, Member member) {
        return reviews.stream().map(review -> {
            boolean isWrited = review.getMemberId().equals(member.getId());
            boolean isLiked = hbtiReviewService.isPresentReviewHeart(review.getId(), member.getId());
            Member author = memberService.findById(review.getMemberId()).get();
            OrderEntity order = orderService.findById(review.getOrderId());
            return new HbtiReviewResponseDto(review, order.getTitle(), author, isWrited, isLiked);
        }).toList();
    }
}
