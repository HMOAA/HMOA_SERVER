package hmoa.hmoaserver.recommend.survey.service;

import hmoa.hmoaserver.exception.Code;
import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.recommend.survey.domain.MemberAnswer;
import hmoa.hmoaserver.recommend.survey.repository.MemberAnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberAnswerService {

    private final MemberAnswerRepository memberAnswerRepository;

    @Transactional
    public MemberAnswer save(MemberAnswer memberAnswer) {
        try {
            return memberAnswerRepository.save(memberAnswer);
        } catch (RuntimeException e) {
            throw new CustomException(e, Code.SERVER_ERROR);
        }
    }

    @Transactional
    public void deleteMemberAnswer(MemberAnswer memberAnswer) {

    }
}
