package hmoa.hmoaserver.term.service;

import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.term.domain.Term;
import hmoa.hmoaserver.term.dto.TermSaveRequestDto;
import hmoa.hmoaserver.term.dto.TermUpdateRequestDto;
import hmoa.hmoaserver.term.repository.TermRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static hmoa.hmoaserver.exception.Code.TERM_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional
public class TermService {

    private final TermRepository termRepository;

    public Term save(TermSaveRequestDto requestDto) {
        return termRepository.save(requestDto.toEntity());
    }

    public List<Term> findTerm() {
        return termRepository.findAll();
    }

    public Term findById(Long termId) {
        Term term = termRepository.findById(termId)
                .orElseThrow(() -> new CustomException(null, TERM_NOT_FOUND));

        if (term.isDeleted() == true) {
            throw new CustomException(null, TERM_NOT_FOUND);
        }

        return term;
    }

    public void updateTermContent(Long termId, TermUpdateRequestDto requestDto) {
        Term foundTerm = findById(termId);
        foundTerm.updateContent(requestDto.getContent());
        termRepository.save(foundTerm);
    }

    public void deleteTerm(Long termId) {
        Term term = findById(termId);

        term.delete();
        termRepository.save(term);
    }
}
