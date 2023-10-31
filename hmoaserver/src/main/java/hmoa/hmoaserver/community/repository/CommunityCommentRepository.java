package hmoa.hmoaserver.community.repository;

import hmoa.hmoaserver.community.domain.CommunityComment;
import hmoa.hmoaserver.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityCommentRepository extends JpaRepository<CommunityComment,Long> {
    Page<CommunityComment> findAllByCommunityId(Long CommunityId, Pageable pageable);

    Page<CommunityComment> findAllByMember(Member member, Pageable pageable);
}
