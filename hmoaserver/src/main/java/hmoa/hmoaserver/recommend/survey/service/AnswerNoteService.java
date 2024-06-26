package hmoa.hmoaserver.recommend.survey.service;

import hmoa.hmoaserver.exception.Code;
import hmoa.hmoaserver.exception.CustomException;

import hmoa.hmoaserver.recommend.survey.domain.AnswerNote;
import hmoa.hmoaserver.recommend.survey.repository.AnswerNoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnswerNoteService {

    private final AnswerNoteRepository answerNoteRepository;

    @Transactional
    public AnswerNote save(AnswerNote answerNote) {
        try {
            return answerNoteRepository.save(answerNote);
        } catch (RuntimeException e) {
            throw new CustomException(e, Code.SERVER_ERROR);
        }
    }
}
