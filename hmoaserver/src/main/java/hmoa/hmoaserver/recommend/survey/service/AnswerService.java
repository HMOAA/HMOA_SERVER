package hmoa.hmoaserver.recommend.survey.service;

import hmoa.hmoaserver.exception.Code;
import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.recommend.survey.domain.Answer;
import hmoa.hmoaserver.recommend.survey.repository.AnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnswerService {

    private final AnswerRepository answerRepository;

    @Transactional
    public Answer save(Answer answer) {
        try {
            return answerRepository.save(answer);
        } catch (RuntimeException e) {
            throw new CustomException(e, Code.SERVER_ERROR);
        }
    }
}
