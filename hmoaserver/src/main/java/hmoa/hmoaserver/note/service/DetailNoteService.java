package hmoa.hmoaserver.note.service;

import hmoa.hmoaserver.exception.Code;
import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.note.domain.DetailNote;
import hmoa.hmoaserver.note.repository.DetailNoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DetailNoteService {

    private final DetailNoteRepository detailNoteRepository;

    public DetailNote save(DetailNote detailNote) {
        try {
            return detailNoteRepository.save(detailNote);
        } catch (RuntimeException e) {
            throw new CustomException(e, Code.SERVER_ERROR);
        }
    }

    @Transactional(readOnly = true)
    public DetailNote findById(Long id) {
        return detailNoteRepository.findById(id).orElseThrow(() -> new CustomException(null, Code.NOTE_NOT_FOUND));
    }
}
