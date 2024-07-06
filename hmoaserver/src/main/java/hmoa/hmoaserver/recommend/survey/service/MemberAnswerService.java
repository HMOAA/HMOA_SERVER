package hmoa.hmoaserver.recommend.survey.service;

import hmoa.hmoaserver.exception.Code;
import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.recommend.survey.domain.Answer;
import hmoa.hmoaserver.recommend.survey.domain.MemberAnswer;
import hmoa.hmoaserver.recommend.survey.repository.MemberAnswerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MemberAnswerService {

    private final MemberAnswerRepository memberAnswerRepository;

    public MemberAnswer save(MemberAnswer memberAnswer) {
        try {
            return memberAnswerRepository.save(memberAnswer);
        } catch (RuntimeException e) {
            throw new CustomException(e, Code.SERVER_ERROR);
        }
    }

    public void deleteMemberAnswer(Member member, Answer answer) {
        MemberAnswer memberAnswer = findByMemberAnswer(member, answer);

        try {
            memberAnswerRepository.delete(memberAnswer);
        } catch (DataAccessException | ConstraintViolationException e) {
            throw new CustomException(null, Code.SERVER_ERROR);
        }
    }

    public MemberAnswer findByMemberAnswer(Member member, Answer answer) {
        return memberAnswerRepository.findByMemberAndAnswer(member, answer).orElseThrow(() -> new CustomException(null, Code.SERVER_ERROR));
    }

    public List<MemberAnswer> findByMember(Member member) {
        return memberAnswerRepository.findAllByMember(member);
    }

    public boolean isExistingMemberAnswer(Member member) {
        return memberAnswerRepository.findAllByMember(member).size() > 0;
    }
}
