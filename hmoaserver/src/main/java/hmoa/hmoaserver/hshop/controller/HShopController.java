package hmoa.hmoaserver.hshop.controller;

import hmoa.hmoaserver.common.PagingDto;
import hmoa.hmoaserver.common.ResultDto;
import hmoa.hmoaserver.hshop.HShopFacade;
import hmoa.hmoaserver.hshop.dto.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Api(tags = {"H-shop"})
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/shop")
public class HShopController {

    private final HShopFacade hShopFacade;

    @ApiOperation("상품 등록")
    @PostMapping("/save")
    public ResponseEntity<ResultDto<Object>> registerProduct(@RequestBody NoteProductSaveRequestDto dto) {
        hShopFacade.registerProduct(dto);
        return ResponseEntity.ok(ResultDto.builder().build());
    }

    @ApiOperation("향료 상품 조회")
    @GetMapping("/note")
    public ResponseEntity<ResultDto<Object>> getNoteProduct(@RequestHeader("X-AUTH-TOKEN") String token) {
        return ResponseEntity.ok(ResultDto.builder().data(hShopFacade.getNoteProducts(token)).build());
    }

    @ApiOperation(value = "구매할 향료 보내기", notes = "상품 조회 시 받은 Product Id 보내주시면 됩니다")
    @PostMapping("/note/select")
    public ResponseEntity<NoteProductsResponseDto> selectNoteProduct(@RequestHeader("X-AUTH-TOKEN") String token, @RequestBody NoteProductSelectRequestDto dto) {
        return ResponseEntity.ok(hShopFacade.selectNoteProducts(token, dto));
    }

    @ApiOperation(value = "향료 주문 요청", notes = "선택한 향료 확인 후 결제 페이지로 넘어가는 API")
    @PostMapping("note/order")
    public ResponseEntity<OrderResponseDto> orderNote(@RequestHeader("X-AUTH-TOKEN") String token, @RequestBody NoteProductSelectRequestDto dto) {
        return ResponseEntity.ok(hShopFacade.orderNotes(token, dto));
    }

    @ApiOperation(value = "향료 주문 정보 조회")
    @GetMapping("note/order/{orderId}")
    public ResponseEntity<OrderInfoResponseDto> getOrderInfo(@RequestHeader("X-AUTH-TOKEN") String token, @PathVariable Long orderId) {
        return ResponseEntity.ok(hShopFacade.getOrderInfos(token, orderId));
    }

    @ApiOperation(value = "결제 페이지에서 선택한 향료 지우기")
    @DeleteMapping("note/order/{orderId}/product/{productId}")
    public ResponseEntity<OrderInfoResponseDto> deleteProduct(@RequestHeader("X-AUTH-TOKEN") String token, @PathVariable Long orderId, @PathVariable Long productId) {
        return ResponseEntity.ok(hShopFacade.deleteProduct(token, orderId, productId));
    }

    @ApiOperation(value = "장바구니 조회", notes = "이전에 선택했던 향료가 존재할 시 없으면 404 에러")
    @GetMapping("/cart")
    public ResponseEntity<NoteProductsResponseDto> findCart(@RequestHeader("X-AUTH-TOKEN") String token) {
        return ResponseEntity.ok(hShopFacade.getCartInfos(token));
    }

    @ApiOperation(value = "Order 내역 지우기 (전부)")
    @DeleteMapping("/order")
    public ResponseEntity<?> deleteOrders(@RequestHeader("X-AUTH-TOKEN") String token) {
        hShopFacade.deleteOrders(token);
        return ResponseEntity.ok(ResultDto.builder().build());
    }

    @ApiOperation(value = "Order 지우기 (id 로)")
    @DeleteMapping("/order/{orderId}")
    public ResponseEntity<?> deleteOrder(@RequestHeader("X-AUTH-TOKEN") String token, @PathVariable Long orderId) {
        hShopFacade.deleteOrder(token, orderId);
        return ResponseEntity.ok(ResultDto.builder().build());
    }

    @ApiOperation(value = "후기 작성 버튼 클릭 시 주문 목록", notes = "후기 작성이 가능한 상태의 주문들만 반환")
    @GetMapping("order/me")
    public ResponseEntity<List<OrderSelectResponseDto>> getSelectReviewList(@RequestHeader("X-AUTH-TOKEN") String token) {
        return ResponseEntity.ok(hShopFacade.getReviewableOrders(token));
    }

    @Tag(name = "H-shop-review", description = "향bti 리뷰 API")
    @ApiOperation(value = "향bti 후기 저장")
    @PostMapping(value = "/review", consumes = "multipart/form-data")
    public ResponseEntity<HbtiReviewResponseDto> saveHbtiReview(@RequestHeader("X-AUTH-TOKEN") String token, @RequestParam Long orderId, @RequestPart(value = "image", required = false) List<MultipartFile> files, HbtiReviewSaveRequestDto dto) {
        return ResponseEntity.ok(hShopFacade.saveHbtiReview(token, orderId, files, dto));
    }

    @Tag(name = "H-shop-review", description = "Hshop review API")
    @ApiOperation(value = "후기 수정")
    @PostMapping(value = "review/{reviewId}", consumes = "multipart/form-data")
    public  ResponseEntity<HbtiReviewResponseDto> modifyHbtiReview(@RequestHeader("X-AUTH-TOKEN") String token, @PathVariable Long reviewId, @RequestPart(value = "image", required = false) List<MultipartFile> photos, HbtiReviewModifyRequestDto dto) {
        return ResponseEntity.ok(hShopFacade.modifyHbtiReview(token, reviewId, photos, dto));
    }

    @Tag(name = "H-shop-review", description = "Hshop review API")
    @ApiOperation(value = "향bti 후기 목록 조회")
    @GetMapping("review")
    public ResponseEntity<PagingDto<Object>> findHbtiReviews(@RequestHeader("X-AUTH-TOKEN") String token, @RequestParam int page) {
        return ResponseEntity.ok(hShopFacade.getHbtiReviews(token, page));
    }

    @Tag(name = "H-shop-review", description = "Hshop review API")
    @ApiOperation(value = "향bti 후기 좋아요")
    @PutMapping("review/{reviewId}/like")
    public ResponseEntity<?> saveHbtiReviewHeart(@RequestHeader("X-AUTH-TOKEN") String token, @PathVariable Long reviewId) {
        hShopFacade.saveHbtiReviewHeart(token, reviewId);
        return ResponseEntity.ok(ResultDto.builder().build());
    }

    @Tag(name = "H-shop-review", description = "Hshop review API")
    @ApiOperation(value = "향bti 후기 좋아요 취소")
    @DeleteMapping("review/{reviewId}/like")
    public ResponseEntity<?> deleteHbtiReviewHeart(@RequestHeader("X-AUTH-TOKEN") String token, @PathVariable Long reviewId) {
        hShopFacade.deleteHbtiReviewHeart(token, reviewId);
        return ResponseEntity.ok(ResultDto.builder().build());
    }

    @Tag(name = "H-shop-review", description = "Hshop review API")
    @ApiOperation(value = "내가 작성한 후기 목록")
    @GetMapping("/review/me")
    public ResponseEntity<?> getMyReviews(@RequestHeader("X-AUTH-TOKEN") String token, @RequestParam Long cursor) {
        return ResponseEntity.ok(hShopFacade.getMyReviews(token, cursor));
    }

    @Tag(name = "H-shop-review", description = "Hshop review API")
    @ApiOperation(value = "후기 삭제")
    @DeleteMapping("/review/{reviewId}")
    public ResponseEntity<?> deleteMyReview(@RequestHeader("X-AUTH-TOKEN") String token, @PathVariable Long reviewId) {
        hShopFacade.deleteReview(token, reviewId);
        return ResponseEntity.ok(ResultDto.builder().build());
    }

    @Tag(name = "H-shop-review", description = "Hshop review API")
    @ApiOperation(value = "후기 단 건 조회")
    @GetMapping("/review/{reviewId}")
    public ResponseEntity<HbtiReviewResponseDto> getReview(@RequestHeader("X-AUTH-TOKEN") String token, @PathVariable Long reviewId) {
        return ResponseEntity.ok(hShopFacade.getReview(token, reviewId));
    }
}
