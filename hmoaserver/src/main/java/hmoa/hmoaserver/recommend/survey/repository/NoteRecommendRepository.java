package hmoa.hmoaserver.recommend.survey.repository;

import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.recommend.survey.domain.NoteRecommend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRecommendRepository extends JpaRepository<NoteRecommend, Long> {
    List<NoteRecommend> findAllByMember(Member member);
}
