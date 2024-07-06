package hmoa.hmoaserver.recommend.survey.service;

import hmoa.hmoaserver.exception.Code;
import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.note.domain.Note;
import hmoa.hmoaserver.recommend.survey.domain.AnswerNote;
import hmoa.hmoaserver.recommend.survey.domain.MemberAnswer;
import hmoa.hmoaserver.recommend.survey.domain.NoteRecommend;
import hmoa.hmoaserver.recommend.survey.repository.NoteRecommendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoteRecommendService {

    private final NoteRecommendRepository noteRecommendRepository;

    @Transactional
    public NoteRecommend save(List<String> notes, Member member) {
        try {
            return noteRecommendRepository.save(NoteRecommend.builder()
                    .member(member)
                    .recommendNotes(notes)
                    .build());
        } catch (RuntimeException e) {
            throw new CustomException(null, Code.SERVER_ERROR);
        }
    }

    @Transactional
    public void delete(NoteRecommend noteRecommend) {
        noteRecommendRepository.delete(noteRecommend);
    }

    public List<NoteRecommend> findByMember(Member member) {
        return noteRecommendRepository.findAllByMember(member);
    }

    public List<String> calculateNoteScoreFromMemberAnswer(List<MemberAnswer> memberAnswers) {
        Map<String, Double> pointMap = new HashMap<>();
        String note;
        double point;

        //응답 결과에서 연관된 note 점수 올리기
        for (MemberAnswer memberAnswer : memberAnswers) {
            for (AnswerNote answerNote : memberAnswer.getAnswer().getAnswerNotes()) {
                note = answerNote.getNote().getTitle();
                point = answerNote.getAnswer().getQuestion().getPoint();

                pointMap.put(note, pointMap.getOrDefault(note, 0.0) + point);
            }
        }

        //pointMap을 내림차순으로 정렬
        List<String> sortedNote = new ArrayList<>(pointMap.keySet());

        sortedNote.sort((o1, o2) -> pointMap.get(o2).compareTo(pointMap.get(o1)));

        return sortedNote;
    }
}
