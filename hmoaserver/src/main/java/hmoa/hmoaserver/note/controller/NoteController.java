package hmoa.hmoaserver.note.controller;

import hmoa.hmoaserver.common.PageUtil;
import hmoa.hmoaserver.common.PagingDto;
import hmoa.hmoaserver.common.ResultDto;
import hmoa.hmoaserver.note.domain.Note;
import hmoa.hmoaserver.note.dto.NoteDefaultResponseDto;
import hmoa.hmoaserver.note.dto.NoteDetailResponseDto;
import hmoa.hmoaserver.note.dto.NoteSaveRequestDto;
import hmoa.hmoaserver.note.dto.NoteUpdateRequestDto;
import hmoa.hmoaserver.note.service.NoteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Api(tags = {"노트"})
@RestController
@RequestMapping("/note")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;

    @ApiOperation("노트 저장")
    @PostMapping("/new")
    public ResponseEntity<ResultDto<Object>> saveNote(NoteSaveRequestDto requestDto) {
        noteService.save(requestDto);

        return ResponseEntity.status(200)
                .body(ResultDto.builder()
                        .build());
    }

    @ApiOperation("노트 목록 조회")
    @GetMapping
    public ResponseEntity<PagingDto<Object>> findNote(@RequestParam int pageNum) {
        Page<Note> notes = noteService.findNote(pageNum);
        boolean isLastPage = PageUtil.isLastPage(notes);

        List<NoteDefaultResponseDto> responseDtos = notes.stream()
                .map(NoteDefaultResponseDto::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(PagingDto.builder()
                .data(responseDtos)
                .isLastPage(isLastPage)
                .build());
    }

    @ApiOperation("노트 목록 조회 (커서 페이징)")
    @GetMapping("/cursor")
    public ResponseEntity<PagingDto<Object>> findNoteByCursor(@RequestParam Long cursor) {
        Page<Note> notes = noteService.findNoteByCursor(cursor);
        boolean isLastPage = PageUtil.isLastPage(notes);

        List<NoteDefaultResponseDto> responseDtos = notes.stream()
                .map(NoteDefaultResponseDto::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(PagingDto.builder()
                .data(responseDtos)
                .isLastPage(isLastPage)
                .build());
    }

    @ApiOperation("노트 단건 조회")
    @GetMapping("/{noteId}")
    public ResponseEntity<ResultDto<Object>> findOneNote(@PathVariable Long noteId) {
        Note note = noteService.findById(noteId);

        NoteDetailResponseDto responseDto = new NoteDetailResponseDto(note);

        return ResponseEntity.status(200)
                .body(ResultDto.builder()
                        .data(responseDto)
                        .build());
    }

    @ApiOperation("노트 내용 수정")
    @PutMapping("/{noteId}/update")
    public ResponseEntity<ResultDto<Object>> updateNoteContent(@PathVariable Long noteId, NoteUpdateRequestDto requestDto) {
        noteService.updateNoteContent(noteId, requestDto);

        return ResponseEntity.status(200)
                .body(ResultDto.builder()
                        .build());
    }

    @ApiOperation("노트 삭제")
    @DeleteMapping("/{noteId}")
    public ResponseEntity<ResultDto<Object>> deleteNote(@PathVariable Long noteId) {
        noteService.deleteNote(noteId);

        return ResponseEntity.status(200)
                .body(ResultDto.builder()
                        .build());
    }
}
