package hmoa.hmoaserver.hshop.controller;

import hmoa.hmoaserver.common.PageSize;
import hmoa.hmoaserver.common.PageUtil;
import hmoa.hmoaserver.common.PagingDto;
import hmoa.hmoaserver.common.ResultDto;
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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Api(tags = {"H-shop"})
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/shop")
public class HShopController {

    private static final int SHIPPING_FEE = 3000;

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

    @ApiOperation("상품 등록")
    @PostMapping("/save")
    public ResponseEntity<ResultDto<Object>> registerProduct(@RequestBody NoteProductSaveRequestDto dto) {

        Note note = noteService.findByTitle(dto.getNoteName());
        NoteProduct noteProduct = dto.toEntity(note);

        noteProductService.save(noteProduct);

        return ResponseEntity.ok(ResultDto.builder().build());
    }

    @ApiOperation("향료 상품 조회")
    @GetMapping("/note")
    public ResponseEntity<ResultDto<Object>> getNoteProduct(@RequestHeader("X-AUTH-TOKEN") String token) {

        Member member = memberService.findByMember(token);

        List<NoteProduct> noteProducts = noteProductService.getAllNoteProducts();
        List<String> recommendNotes = member.getNoteRecommend().getRecommendNotes();
        List<NoteProductResponseDto> result = new ArrayList<>();

        for (NoteProduct noteProduct : noteProducts) {
            boolean recommend = recommendNotes.contains(noteProduct.getNote().getTitle());
            result.add(new NoteProductResponseDto(noteProduct, recommend));
        }

        return ResponseEntity.ok(ResultDto.builder().data(result).build());
    }

    @ApiOperation(value = "구매할 향료 보내기", notes = "상품 조회 시 받은 Product Id 보내주시면 됩니다")
    @PostMapping("/note/select")
    public ResponseEntity<NoteProductsResponseDto> selectNoteProduct(@RequestHeader("X-AUTH-TOKEN") String token, @RequestBody NoteProductSelectRequestDto dto) {

        Member member = memberService.findByMember(token);
        Optional<Cart> cart = cartService.findOneCartByMemberId(member.getId());
        NoteProductsResponseDto result = noteProductService.getNoteProducts(dto.getProductIds());

        if (cart.isPresent()) {
            cartService.updateCart(cart.get(), dto.getProductIds(), result.getTotalPrice());
            return ResponseEntity.ok(result);
        }

        cartService.save(dto.toCartEntity(member.getId(), result.getTotalPrice()));
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "향료 주문 요청", notes = "선택한 향료 확인 후 결제 페이지로 넘어가는 API")
    @PostMapping("note/order")
    public ResponseEntity<OrderResponseDto> orderNote(@RequestHeader("X-AUTH-TOKEN") String token, @RequestBody NoteProductSelectRequestDto dto) {

        Member member = memberService.findByMember(token);

        NoteProductsResponseDto noteProducts = noteProductService.getNoteProducts(dto.getProductIds());
        String orderTitle = noteProducts.getNoteProducts().get(0).getProductName();
        OrderEntity order = orderService.firstOrderSave(member, orderTitle, dto.getProductIds(), noteProducts.getTotalPrice());
        boolean isExistMemberInfo = memberInfoService.isExistMemberInfo(member.getId());
        boolean isExistMemberAddress = memberAddressService.isExistMemberAddress(member.getId());

        return ResponseEntity.ok(new OrderResponseDto(order, isExistMemberInfo, isExistMemberAddress));
    }

    @ApiOperation(value = "향료 주문 정보 조회")
    @GetMapping("note/order/{orderId}")
    public ResponseEntity<OrderInfoResponseDto> getOrderInfo(@RequestHeader("X-AUTH-TOKEN") String token, @PathVariable Long orderId) {

        Member member = memberService.findByMember(token);
        OrderEntity order = orderService.findById(orderId);

        memberService.checkAuthorization(member.getId(), order.getMemberId());

        NoteProductsResponseDto noteProducts = noteProductService.getNoteProducts(order.getProductIds());

        return ResponseEntity.ok(new OrderInfoResponseDto(noteProducts, order.getTotalPrice(), SHIPPING_FEE));
    }

    @ApiOperation(value = "결제 페이지에서 선택한 향료 지우기")
    @DeleteMapping("note/order/{orderId}/product/{productId}")
    public ResponseEntity<OrderInfoResponseDto> deleteProduct(@RequestHeader("X-AUTH-TOKEN") String token, @PathVariable Long orderId, @PathVariable Long productId) {

        Member member = memberService.findByMember(token);
        OrderEntity order = orderService.findById(orderId);
        NoteProduct product = noteProductService.getNoteProduct(productId);

        memberService.checkAuthorization(member.getId(), order.getMemberId());
        orderService.deleteProduct(order, product);

        NoteProductsResponseDto noteProducts = noteProductService.getNoteProducts(order.getProductIds());

        return ResponseEntity.ok(new OrderInfoResponseDto(noteProducts, order.getTotalPrice(), SHIPPING_FEE));
    }

    @ApiOperation(value = "장바구니 조회", notes = "이전에 선택했던 향료가 존재할 시 없으면 404 에러")
    @GetMapping("/cart")
    public ResponseEntity<NoteProductsResponseDto> findCart(@RequestHeader("X-AUTH-TOKEN") String token) {

        Member member = memberService.findByMember(token);

        Cart cart = cartService.findOneCartByMemberId(member.getId()).orElseThrow(() -> new CustomException(null, Code.CART_NOT_FOUND));

        return ResponseEntity.ok(noteProductService.getNoteProducts(cart.getProductIds()));
    }

    @ApiOperation(value = "Order 내역 지우기 (전부)")
    @DeleteMapping("/order")
    public ResponseEntity<?> deleteOrders(@RequestHeader("X-AUTH-TOKEN") String token) {
        Member member = memberService.findByMember(token);
        List<OrderEntity> orders = orderService.findByMemberId(member.getId());
        orderService.deleteOrders(orders);

        return ResponseEntity.ok(ResultDto.builder().build());
    }

    @ApiOperation(value = "Order 지우기 (id 로)")
    @DeleteMapping("/order/{orderId}")
    public ResponseEntity<?> deleteOrder(@RequestHeader("X-AUTH-TOKEN") String token, @PathVariable Long orderId) {
        OrderEntity order = orderService.findById(orderId);
        orderService.deleteOrder(order);

        return ResponseEntity.ok(ResultDto.builder().build());
    }

    @ApiOperation(value = "후기 작성 버튼 클릭 시 주문 목록")
    @GetMapping("order/me")
    public ResponseEntity<List<OrderSelectResponseDto>> getSelectReviewList(@RequestHeader("X-AUTH-TOKEN") String token) {
        Member member = memberService.findByMember(token);
        Page<OrderEntity> orders = orderService.getOrderPage(member.getId(), PageSize.ZERO_PAGE.getSize());
        List<OrderSelectResponseDto> res =orders.stream().map(OrderSelectResponseDto::new).toList();

        return ResponseEntity.ok(res);
    }

    @Tag(name = "H-shop-review", description = "향bti 리뷰 API")
    @ApiOperation(value = "향bti 후기 저장")
    @PostMapping(value = "/review", consumes = "multipart/form-data")
    public ResponseEntity<HbtiReviewResponseDto> saveHbtiReview(@RequestHeader("X-AUTH-TOKEN") String token, @RequestParam Long orderId, @RequestPart(value = "image", required = false) List<MultipartFile> files, HbtiReviewSaveRequestDto dto) {
        Member member = memberService.findByMember(token);
        OrderEntity order = orderService.findById(orderId);

        HbtiReview hbtiReview = hbtiReviewService.save(dto.toEntity(member.getId(), order.getId()));
        orderService.updateOrderStatus(order, OrderStatus.REVIEW_COMPLETE);

        List<HbtiPhoto> photos = new ArrayList<>();

        if (files != null) {
            photoService.validateReviewPhotoCountExceeded(files.size());
            photos = hbtiReviewService.saveHbtiPhotos(hbtiReview, files);
        }

        return ResponseEntity.ok(createReviewResponseDto(hbtiReview, order, member, photos));
    }

    @Tag(name = "H-shop-review", description = "Hshop review API")
    @ApiOperation(value = "후기 수정")
    @PostMapping(value = "review/{reviewId}", consumes = "multipart/form-data")
    public  ResponseEntity<HbtiReviewResponseDto> modifyHbtiReview(@RequestHeader("X-AUTH-TOKEN") String token, @PathVariable Long reviewId, @RequestPart(value = "image", required = false) List<MultipartFile> photos, HbtiReviewModifyRequestDto dto) {
        Member member = memberService.findByMember(token);
        HbtiReview review = hbtiReviewService.getReview(reviewId);
        OrderEntity order = orderService.findById(review.getOrderId());

        validateOwner(member, review);

        List<HbtiPhoto> curPhoto = new ArrayList<>(review.getHbtiPhotos().stream()
                .filter(photo -> !dto.getDeleteReviewPhotoIds().contains(photo.getId()))
                .toList());

        hbtiPhotoService.deleteAll(dto.getDeleteReviewPhotoIds());
        hbtiReviewService.modifyHbtiReview(review, dto);

        if (photos != null) {
            curPhoto.addAll(hbtiReviewService.saveHbtiPhotos(review, photos));
        }

        return ResponseEntity.ok(createReviewResponseDto(review, order, member, curPhoto));
    }

    @Tag(name = "H-shop-review", description = "Hshop review API")
    @ApiOperation(value = "향bti 후기 목록 조회")
    @GetMapping("review")
    public ResponseEntity<PagingDto<Object>> findHbtiReviews(@RequestHeader("X-AUTH-TOKEN") String token, @RequestParam int page) {
        Member member = memberService.findByMember(token);
        Page<HbtiReview> hbtiReviews = hbtiReviewService.getHbtiReviewsByPage(page);
        List<HbtiReviewResponseDto> res = createReviewResponseDtos(hbtiReviews, member);
        boolean isLastPage = PageUtil.isLastPage(hbtiReviews);

        return ResponseEntity.ok(PagingDto.builder()
                .isLastPage(isLastPage)
                .data(res)
                .build());
    }

    @Tag(name = "H-shop-review", description = "Hshop review API")
    @ApiOperation(value = "향bti 후기 좋아요")
    @PutMapping("review/{reviewId}/like")
    public ResponseEntity<?> saveHbtiReviewHeart(@RequestHeader("X-AUTH-TOKEN") String token, @PathVariable Long reviewId) {
        Member member = memberService.findByMember(token);
        HbtiReview review = hbtiReviewService.getReview(reviewId);

        hbtiReviewService.saveHeart(review.getId(), member.getId());
        hbtiReviewService.increaseHbtiHeartCount(review);
        fcmNotificationService.sendNotification(new FCMNotificationRequestDto(review.getMemberId(), member.getNickname(), member.getId(), NotificationType.HBTI_REVIEW_LIKE, reviewId));

        return ResponseEntity.ok(ResultDto.builder().build());
    }

    @Tag(name = "H-shop-review", description = "Hshop review API")
    @ApiOperation(value = "향bti 후기 좋아요 취소")
    @DeleteMapping("review/{reviewId}/like")
    public ResponseEntity<?> deleteHbtiReviewHeart(@RequestHeader("X-AUTH-TOKEN") String token, @PathVariable Long reviewId) {
        Member member = memberService.findByMember(token);
        HbtiReview review = hbtiReviewService.getReview(reviewId);

        hbtiReviewService.deleteHeart(review.getId(), member.getId());
        hbtiReviewService.decreaseHbtiHeartCount(review);

        return ResponseEntity.ok(ResultDto.builder().build());
    }

    @Tag(name = "H-shop-review", description = "Hshop review API")
    @ApiOperation(value = "내가 작성한 후기 목록")
    @GetMapping("/review/me")
    public ResponseEntity<?> getMyReviews(@RequestHeader("X-AUTH-TOKEN") String token, @RequestParam Long cursor) {
        Member member = memberService.findByMember(token);
        if (PageUtil.isFistCursor(cursor)) cursor = PageUtil.convertFirstCursor(cursor);
        Page<HbtiReview> reviews = hbtiReviewService.getHbtiReviewsByMemberAndCursor(member.getId(), cursor);
        List<HbtiReviewResponseDto> res = createReviewResponseDtos(reviews, member);
        boolean isLastPage = PageUtil.isLastPage(reviews);

        return ResponseEntity.ok(PagingDto.builder()
                .isLastPage(isLastPage)
                .data(res)
                .build());
    }

    @Tag(name = "H-shop-review", description = "Hshop review API")
    @ApiOperation(value = "후기 삭제")
    @DeleteMapping("/review/{reviewId}")
    public ResponseEntity<?> deleteMyReview(@RequestHeader("X-AUTH-TOKEN") String token, @PathVariable Long reviewId) {
        Member member = memberService.findByMember(token);
        HbtiReview review = hbtiReviewService.getReview(reviewId);

        validateOwner(member, review);

        List<HbtiReviewHeart> hearts = hbtiReviewService.getReviewHeartsByReviewId(reviewId);
        hbtiReviewService.deleteHbtiReviewHeart(hearts);
        hbtiReviewService.deleteHbtiReview(review);

        return ResponseEntity.ok(ResultDto.builder().build());
    }

    @Tag(name = "H-shop-review", description = "Hshop review API")
    @ApiOperation(value = "후기 단 건 조회")
    @GetMapping("/review/{reviewId}")
    public ResponseEntity<HbtiReviewResponseDto> getReview(@RequestHeader("X-AUTH-TOKEN") String token, @PathVariable Long reviewId) {
        Member member = memberService.findByMember(token);
        HbtiReview review = hbtiReviewService.getReview(reviewId);

        boolean isWrited = review.getMemberId().equals(member.getId());
        boolean isLiked = hbtiReviewService.isPresentReviewHeart(reviewId, member.getId());
        Member author = memberService.findById(review.getMemberId()).get();
        OrderEntity order = orderService.findById(review.getOrderId());

        return ResponseEntity.ok(new HbtiReviewResponseDto(review, order.getTitle(), author, isWrited, isLiked));
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

    private List<HbtiReviewResponseDto> createReviewResponseDtos(Page<HbtiReview> reviews, Member member) {
        return reviews.stream().map(review -> {
            boolean isWrited = review.getMemberId().equals(member.getId());
            boolean isLiked = hbtiReviewService.isPresentReviewHeart(review.getId(), member.getId());
            Member author = memberService.findById(review.getMemberId()).get();
            OrderEntity order = orderService.findById(review.getOrderId());
            return new HbtiReviewResponseDto(review, order.getTitle(), author, isWrited, isLiked);
        }).toList();
    }
}
