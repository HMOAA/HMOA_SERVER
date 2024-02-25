package hmoa.hmoaserver.community.repository;

import hmoa.hmoaserver.community.domain.Community;
import hmoa.hmoaserver.community.domain.CommunityLikedMember;
import hmoa.hmoaserver.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommunityLikedMemberRepository extends JpaRepository<CommunityLikedMember, Long> {
    Optional<CommunityLikedMember> findByMemberAndCommunity(Member member, Community community);

}
