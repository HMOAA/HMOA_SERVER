package hmoa.hmoaserver.recommend.survey.repository;

import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.recommend.survey.domain.Answer;
import hmoa.hmoaserver.recommend.survey.domain.MemberAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberAnswerRepository extends JpaRepository<MemberAnswer, Long> {
    List<MemberAnswer> findAllByMember(Member member);
    Optional<MemberAnswer> findByMemberAndAnswer(Member member, Answer answer);
}
