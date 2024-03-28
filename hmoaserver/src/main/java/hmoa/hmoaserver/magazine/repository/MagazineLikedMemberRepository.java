package hmoa.hmoaserver.magazine.repository;

import hmoa.hmoaserver.magazine.domain.Magazine;
import hmoa.hmoaserver.magazine.domain.MagazineLikedMember;
import hmoa.hmoaserver.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MagazineLikedMemberRepository extends JpaRepository<MagazineLikedMember, Long> {
    Optional<MagazineLikedMember> findByMagazineAndMember(Magazine magazine, Member member);
}
