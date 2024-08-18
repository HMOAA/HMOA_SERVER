package hmoa.hmoaserver.hshop.service;

import hmoa.hmoaserver.exception.Code;
import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.hshop.domain.NoteProduct;
import hmoa.hmoaserver.hshop.dto.NoteProductDetailResponseDto;
import hmoa.hmoaserver.hshop.repository.NoteProductRepository;
import hmoa.hmoaserver.note.dto.NoteWithDetailNotesDto;
import hmoa.hmoaserver.note.service.NoteService;
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
    private final NoteService noteService;

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

    @Transactional(readOnly = true)
    public NoteProductDetailResponseDto getNoteProductDetail(Long id) {
        NoteProduct noteProduct = getNoteProduct(id);
        NoteWithDetailNotesDto noteDetail = noteService.getDetailNotes(noteProduct.getNote().getId());
        return new NoteProductDetailResponseDto(id, noteDetail, noteProduct.getPrice());
    }
}
