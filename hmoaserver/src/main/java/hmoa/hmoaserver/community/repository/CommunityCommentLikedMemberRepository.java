package hmoa.hmoaserver.community.repository;

import hmoa.hmoaserver.community.domain.CommunityComment;
import hmoa.hmoaserver.community.domain.CommunityCommentLikedMember;
import hmoa.hmoaserver.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommunityCommentLikedMemberRepository extends JpaRepository<CommunityCommentLikedMember, Long> {
    Optional<CommunityCommentLikedMember> findByMemberAndCommunityComment(Member member, CommunityComment communityComment);
}
