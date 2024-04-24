package hmoa.hmoaserver.community.repository;

import hmoa.hmoaserver.community.domain.CommunityComment;
import hmoa.hmoaserver.community.domain.CommunityCommentLikedMember;
import hmoa.hmoaserver.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CommunityCommentLikedMemberRepository extends JpaRepository<CommunityCommentLikedMember, Long> {
    Optional<CommunityCommentLikedMember> findByMemberAndCommunityComment(Member member, CommunityComment communityComment);

    Page<CommunityCommentLikedMember> findAllByMemberOrderByIdDesc(Member member, Pageable pageable);

    @Query("SELECT cclm " +
            "FROM CommunityCommentLikedMember cclm " +
            "WHERE cclm.member = ?1 " +
            "AND cclm.id < ?2 " +
            "ORDER BY cclm.id DESC")
    Page<CommunityCommentLikedMember> findAllByMemberNextCursor(Member member, Long cursor, Pageable pageable);
}
