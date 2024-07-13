package hmoa.hmoaserver.hshop.controller;

import hmoa.hmoaserver.common.ResultDto;
import hmoa.hmoaserver.hshop.domain.NoteProduct;
import hmoa.hmoaserver.hshop.dto.NoteProductResponseDto;
import hmoa.hmoaserver.hshop.dto.NoteProductSaveRequestDto;
import hmoa.hmoaserver.hshop.service.NoteProductService;
import hmoa.hmoaserver.member.domain.Member;
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

@Api(tags = {"H-shop"})
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/shop")
public class HShopController {

    private final NoteProductService noteProductService;
    private final NoteService noteService;
    private final MemberService memberService;

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
            boolean recommend = false;
            if (recommendNotes.contains(noteProduct.getNote().getTitle())) {
                recommend = true;
            }
            result.add(new NoteProductResponseDto(noteProduct, recommend));
        }

        return ResponseEntity.ok(ResultDto.builder().data(result).build());
    }
}
