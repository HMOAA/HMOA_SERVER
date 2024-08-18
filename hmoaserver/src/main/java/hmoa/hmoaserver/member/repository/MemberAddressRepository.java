package hmoa.hmoaserver.member.repository;

import hmoa.hmoaserver.member.domain.MemberAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberAddressRepository extends JpaRepository<MemberAddress, Long> {

    List<MemberAddress> findByMemberId(Long memberId);
}
