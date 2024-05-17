package hmoa.hmoaserver.note.service;

import hmoa.hmoaserver.common.PageSize;
import hmoa.hmoaserver.common.PageUtil;
import hmoa.hmoaserver.exception.Code;
import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.note.domain.Note;
import hmoa.hmoaserver.note.dto.NoteSaveRequestDto;
import hmoa.hmoaserver.note.dto.NoteUpdateRequestDto;
import hmoa.hmoaserver.note.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static hmoa.hmoaserver.exception.Code.NOTE_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional
public class NoteService {

    private final NoteRepository noteRepository;
    private static final PageRequest DEFAULT_PAGE_REQUEST = PageRequest.of(PageSize.ZERO_PAGE.getSize(), PageSize.FIFTY_SIZE.getSize());

    public Note save(NoteSaveRequestDto requestDto) {
        return noteRepository.save(requestDto.toEntity());
    }

    public Page<Note> findNote(int pageNum) {
        return noteRepository.findAll(PageRequest.of(pageNum, PageSize.FIFTY_SIZE.getSize()));
    }

    public Page<Note> findNoteByCursor(Long cursor) {
        if (PageUtil.isFistCursor(cursor)) {
            return noteRepository.findAllByOrderByIdDesc(DEFAULT_PAGE_REQUEST);
        }
        return noteRepository.findNoteNextPage(cursor, DEFAULT_PAGE_REQUEST);
    }

    public Note findById(Long noteId) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new CustomException(null, NOTE_NOT_FOUND));

        if (note.isDeleted()) {
            throw new CustomException(null, NOTE_NOT_FOUND);
        }

        return note;
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
