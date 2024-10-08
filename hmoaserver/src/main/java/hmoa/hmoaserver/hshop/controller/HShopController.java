package hmoa.hmoaserver.hshop.controller;

import hmoa.hmoaserver.common.PageSize;
import hmoa.hmoaserver.common.PageUtil;
import hmoa.hmoaserver.common.PagingDto;
import hmoa.hmoaserver.common.ResultDto;
import hmoa.hmoaserver.exception.Code;
import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.hshop.domain.Cart;
import hmoa.hmoaserver.hshop.domain.HbtiReview;
import hmoa.hmoaserver.hshop.domain.NoteProduct;
import hmoa.hmoaserver.hshop.domain.OrderEntity;
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
import java.util.stream.Collectors;

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
    public ResponseEntity<HbtiReviewResponseDto> saveHbtiReview(@RequestHeader("X-AUTH-TOKEN") String token, @RequestParam Long orderId, @RequestPart(value="image", required = false) List<MultipartFile> files, HbtiReviewSaveRequestDto dto) {
        Member member = memberService.findByMember(token);
        OrderEntity order = orderService.findById(orderId);

        Note orderNote = noteService.findById(noteProductService.getNoteProduct(order.getProductIds().get(0)).getId());
        HbtiReview hbtiReview = hbtiReviewService.save(dto.toEntity(member.getId(), order.getId()));
        List<HbtiPhoto> photos = new ArrayList<>();

        if (files != null) {
            photoService.validateCommunityPhotoCountExceeded(files.size());
            photos = hbtiReviewService.saveHbtiPhotos(hbtiReview, files);
        }

        HbtiReviewResponseDto res = new HbtiReviewResponseDto(hbtiReview, orderNote.getTitle(), member, true, false);
        res.setHbtiPhotos(photos.stream().map(PhotoResponseDto::new).toList());
        res.setImagesCount(photos.size());

        return ResponseEntity.ok(res);
    }

    @Tag(name = "H-shop-review", description = "Hshop review API")
    @ApiOperation(value = "향bti 후기 목록 조회")
    @GetMapping("review")
    public ResponseEntity<PagingDto<Object>> findHbtiReviews(@RequestHeader("X-AUTH-TOKEN") String token, @RequestParam int page) {
        Member member = memberService.findByMember(token);
        Page<HbtiReview> hbtiReviews = hbtiReviewService.getHbtiReviewsByPage(page);
        List<HbtiReviewResponseDto> res = hbtiReviews.stream().map(hbtiReview -> {
            boolean isWrited = hbtiReview.getMemberId().equals(member.getId());
            boolean isLiked = hbtiReviewService.isPresentReviewHeart(hbtiReview.getId(), member.getId());
            Member author = memberService.findById(hbtiReview.getMemberId()).get();
            OrderEntity order = orderService.findById(hbtiReview.getOrderId());
            return new HbtiReviewResponseDto(hbtiReview, order.getTitle(), author, isWrited, isLiked);
        }).toList();
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
}
