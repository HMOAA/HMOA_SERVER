package hmoa.hmoaserver.community.repository;

import hmoa.hmoaserver.community.domain.CommunityComment;
import hmoa.hmoaserver.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommunityCommentRepository extends JpaRepository<CommunityComment,Long> {
    Page<CommunityComment> findAllByCommunityIdOrderByCreatedAtDescIdDesc(Long communityId, Pageable pageable);
    Page<CommunityComment> findAllByMemberOrderByCreatedAtDescIdDesc(Member member, Pageable pageable);

    @Query("SELECT cc " +
            "FROM CommunityComment cc " +
            "WHERE cc.community.id = ?1 " +
            "AND cc.id < ?2 " +
            "ORDER BY cc.createdAt DESC, cc.id DESC")
    Page<CommunityComment> findCommunityCommentNextPage(Long communityId, Long cursor, Pageable pageable);

    @Query("SELECT cc " +
            "FROM CommunityComment cc " +
            "WHERE cc.member = ?1 " +
            "AND cc.id < ?2 " +
            "ORDER BY cc.createdAt DESC, cc.id DESC")
    Page<CommunityComment> findCommunityCommentByMemberNextPage(Member member, Long cursor, Pageable pageable);

    Long countByCommunityId(Long communityId);
}
