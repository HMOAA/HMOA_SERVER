package hmoa.hmoaserver.recommend.survey.repository;

import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.recommend.survey.domain.MemberAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberAnswerRepository extends JpaRepository<MemberAnswer, Long> {
    Optional<MemberAnswer> findByMember(Member member);
}
