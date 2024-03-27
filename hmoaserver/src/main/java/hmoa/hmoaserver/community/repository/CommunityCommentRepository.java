package hmoa.hmoaserver.community.repository;

import hmoa.hmoaserver.community.domain.CommunityComment;
import hmoa.hmoaserver.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommunityCommentRepository extends JpaRepository<CommunityComment,Long> {
    Page<CommunityComment> findAllByCommunityIdOrderByCreatedAtDescIdAsc(Long communityId, Pageable pageable);
    Page<CommunityComment> findAllByMember(Member member, Pageable pageable);

    @Query("SELECT c " +
            "FROM CommunityComment c " +
            "WHERE c.community.id = ?1 " +
            "AND c.id < ?2 " +
            "ORDER BY c.createdAt DESC, c.id ASC")
    Page<CommunityComment> findCommunityCommentNextPage(Long communityId, Long cursor, Pageable pageable);
}
