package hmoa.hmoaserver.recommend.survey.service;

import hmoa.hmoaserver.exception.Code;
import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.recommend.survey.domain.Question;
import hmoa.hmoaserver.recommend.survey.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuestionService {

    private final QuestionRepository questionRepository;

    @Transactional
    public Question save(Question question) {
        try {
            return questionRepository.save(question);
        } catch (RuntimeException e) {
            throw new CustomException(e, Code.SERVER_ERROR);
        }
    }
}
