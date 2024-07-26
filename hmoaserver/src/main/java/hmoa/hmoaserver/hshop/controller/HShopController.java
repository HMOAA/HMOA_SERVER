package hmoa.hmoaserver.hshop.controller;

import hmoa.hmoaserver.common.ResultDto;
import hmoa.hmoaserver.exception.Code;
import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.hshop.domain.Cart;
import hmoa.hmoaserver.hshop.domain.NoteProduct;
import hmoa.hmoaserver.hshop.domain.OrderEntity;
import hmoa.hmoaserver.hshop.dto.*;
import hmoa.hmoaserver.hshop.service.CartService;
import hmoa.hmoaserver.hshop.service.NoteProductService;
import hmoa.hmoaserver.hshop.service.OrderService;
import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.member.domain.MemberAddress;
import hmoa.hmoaserver.member.dto.MemberAddressResponseDto;
import hmoa.hmoaserver.member.dto.MemberInfoResponseDto;
import hmoa.hmoaserver.member.service.MemberService;
import hmoa.hmoaserver.note.domain.Note;
import hmoa.hmoaserver.note.service.NoteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    private final OrderService orderService;
    private final CartService cartService;

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
        NoteProductsResponseDto result = getNoteProductDetails(dto.getProductIds());

        if (cart.isPresent()) {
            cartService.updateCart(cart.get(), dto.getProductIds(), result.getTotalPrice());
            return ResponseEntity.ok(result);
        }

        cartService.save(dto.toCartEntity(member.getId(), result.getTotalPrice()));
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "주문 요청", notes = "선택한 향료 확인 후 결제 페이지로 넘어가는 API")
    @PostMapping("note/order")
    public ResponseEntity<OrderResponseDto> orderNote(@RequestHeader("X-AUTH-TOKEN") String token, @RequestBody NoteProductSelectRequestDto dto) {

        Member member = memberService.findByMember(token);

        NoteProductsResponseDto noteProducts = getNoteProductDetails(dto.getProductIds());
        OrderEntity order = orderService.firstOrderSave(member, dto.getProductIds(), noteProducts.getTotalPrice());
        MemberAddressResponseDto memberAddress = new MemberAddressResponseDto(memberService.getMemberAddress(member));
        MemberInfoResponseDto memberInfo = new MemberInfoResponseDto(memberService.getMemberInfo(member));

        return ResponseEntity.ok(new OrderResponseDto(order, memberInfo, memberAddress, noteProducts, SHIPPING_FEE));
    }

    @ApiOperation(value = "장바구니 조회", notes = "이전에 선택했던 향료가 존재할 시 없으면 404 에러")
    @GetMapping("/cart")
    public ResponseEntity<NoteProductsResponseDto> findCart(@RequestHeader("X-AUTH-TOKEN") String token) {

        Member member = memberService.findByMember(token);

        Cart cart = cartService.findOneCartByMemberId(member.getId()).orElseThrow(() -> new CustomException(null, Code.CART_NOT_FOUND));

        return ResponseEntity.ok(getNoteProductDetails(cart.getProductIds()));
    }

    private NoteProductsResponseDto getNoteProductDetails(List<Long> productIds) {

        int totalPrice = 0;
        List<NoteProductDetailResponseDto> noteProducts = new ArrayList<>();

        for (Long productId : productIds) {
            NoteProductDetailResponseDto noteProductDetailResponseDto = noteProductService.getNoteProductDetail(productId);
            noteProducts.add(noteProductDetailResponseDto);
            totalPrice += noteProductDetailResponseDto.getPrice();
        }

        return new NoteProductsResponseDto(totalPrice, noteProducts);
    }
}
