package hmoa.hmoaserver.note.service;

import hmoa.hmoaserver.common.PageSize;
import hmoa.hmoaserver.common.PageUtil;
import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.note.domain.Note;
import hmoa.hmoaserver.note.domain.NoteDetailNote;
import hmoa.hmoaserver.note.dto.*;
import hmoa.hmoaserver.note.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static hmoa.hmoaserver.exception.Code.NOTE_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional
public class NoteService {

    private final NoteRepository noteRepository;

    private static final PageRequest DEFAULT_PAGE_REQUEST = PageRequest.of(PageSize.ZERO_PAGE.getSize(), PageSize.FIFTY_SIZE.getSize());
    private static final Long[] NOTE_DETAIL_IDS = new Long[]{1L, 2L, 3L, 5L, 6L, 7L, 14L, 16L};

    public Note save(NoteSaveRequestDto requestDto) {
        return noteRepository.save(requestDto.toEntity());
    }

    @Transactional(readOnly = true)
    public Page<Note> findNote(int pageNum) {
        return noteRepository.findAll(PageRequest.of(pageNum, PageSize.FIFTY_SIZE.getSize()));
    }

    @Transactional(readOnly = true)
    public Note findByTitle(String title) {
        return noteRepository.findByTitle(title).orElseThrow(() -> new CustomException(null, NOTE_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public Page<Note> findNoteByCursor(Long cursor) {
        if (PageUtil.isFistCursor(cursor)) {
            return noteRepository.findAllByOrderByIdDesc(DEFAULT_PAGE_REQUEST);
        }
        return noteRepository.findNoteNextPage(cursor, DEFAULT_PAGE_REQUEST);
    }

    @Transactional(readOnly = true)
    public Note findById(Long noteId) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new CustomException(null, NOTE_NOT_FOUND));

        if (note.isDeleted()) {
            throw new CustomException(null, NOTE_NOT_FOUND);
        }

        return note;
    }

    @Transactional(readOnly = true)
    public List<Note> getNotesWithDetails() {
        return Arrays.stream(NOTE_DETAIL_IDS).map(id -> noteRepository.findByIdWithDetails(id).get()).collect(Collectors.toList());
    }

    //join fetch로 N + 1 문제 방지
    @Transactional(readOnly = true)
    public Note findByIdWithDetail(Long noteId) {
        return noteRepository.findByIdWithDetails(noteId).orElseThrow(() -> new CustomException(null, NOTE_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public NoteWithDetailNotesDto getDetailNotes(Long noteId) {
        Note note = findByIdWithDetail(noteId);
        List<NoteDetailNote> noteDetailNotes = note.getNoteDetailNotes();
        List<DetailNoteResponseDto> dto = noteDetailNotes.stream()
                .map(noteDetailNote -> new DetailNoteResponseDto(noteDetailNote.getDetailNote())).collect(Collectors.toList());

        return new NoteWithDetailNotesDto(note, dto);
    }

    public void updateNoteContent(Long noteId, NoteUpdateRequestDto requestDto) {
        Note foundNote = findById(noteId);
        foundNote.updateContent(requestDto.getContent());
        noteRepository.save(foundNote);
    }

    public void deleteNote(Long noteId) {
        Note note = findById(noteId);

        note.delete();;
        noteRepository.save(note);
    }
}
