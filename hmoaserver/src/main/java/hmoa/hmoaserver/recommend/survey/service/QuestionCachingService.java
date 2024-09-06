package hmoa.hmoaserver.recommend.survey.service;

import hmoa.hmoaserver.config.CacheName;
import hmoa.hmoaserver.recommend.survey.domain.Question;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionCachingService {
    private final QuestionService questionService;

    @Cacheable(cacheNames = CacheName.QUESTION, key = "#questionId")
    public Question getQuestionById(Long questionId) {
        return questionService.getQuestion(questionId);
    }
}
