package hmoa.hmoaserver.recommend.survey.service;

import hmoa.hmoaserver.exception.Code;
import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.recommend.survey.domain.NoteRecommend;
import hmoa.hmoaserver.recommend.survey.repository.NoteRecommendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoteRecommendService {

    private final NoteRecommendRepository noteRecommendRepository;

    @Transactional
    public NoteRecommend save(NoteRecommend noteRecommend) {
        try {
            return noteRecommendRepository.save(noteRecommend);
        } catch (RuntimeException e) {
            throw new CustomException(null, Code.SERVER_ERROR);
        }
    }
}
