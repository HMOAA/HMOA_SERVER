package hmoa.hmoaserver.note.service;

import hmoa.hmoaserver.exception.Code;
import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.note.domain.NoteDetailNote;
import hmoa.hmoaserver.note.repository.NoteDetailNoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class NoteDetailNoteService {

    private final NoteDetailNoteRepository noteDetailNoteRepository;

    public NoteDetailNote save(NoteDetailNote noteDetailNote) {
        try {
            return noteDetailNoteRepository.save(noteDetailNote);
        } catch (RuntimeException e) {
            throw new CustomException(e, Code.SERVER_ERROR);
        }
    }
}
