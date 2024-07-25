package hmoa.hmoaserver.hshop.service;

import hmoa.hmoaserver.exception.Code;
import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.hshop.domain.NoteProduct;
import hmoa.hmoaserver.hshop.repository.NoteProductRepository;
import hmoa.hmoaserver.note.domain.Note;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class NoteProductService {

    private final NoteProductRepository noteProductRepository;

    public NoteProduct save(NoteProduct noteProduct) {
        try {
            return noteProductRepository.save(noteProduct);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(e, Code.SERVER_ERROR);
        }
    }

    @Transactional(readOnly = true)
    public List<NoteProduct> getAllNoteProducts() {
        return noteProductRepository.findAll();
    }

    @Transactional(readOnly = true)
    public NoteProduct getNoteProduct(Long id) {
        return noteProductRepository.findById(id).orElseThrow(() -> new CustomException(null, Code.NOTE_NOT_FOUND));
    }
}