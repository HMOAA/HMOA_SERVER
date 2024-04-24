package hmoa.hmoaserver.community.repository;

import hmoa.hmoaserver.community.domain.Community;
import hmoa.hmoaserver.community.domain.CommunityLikedMember;
import hmoa.hmoaserver.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CommunityLikedMemberRepository extends JpaRepository<CommunityLikedMember, Long> {
    Optional<CommunityLikedMember> findByMemberAndCommunity(Member member, Community community);
    Page<CommunityLikedMember> findAllByMemberOrderByIdDesc(Member member, Pageable pageable);

    @Query("SELECT clm " +
            "FROM CommunityLikedMember clm " +
            "WHERE clm.member = ?1 " +
            "AND clm.id < ?2 " +
            "ORDER BY clm.id DESC")
    Page<CommunityLikedMember> findAllByMemberNextCursor(Member member, Long cursor, Pageable pageable);

}
