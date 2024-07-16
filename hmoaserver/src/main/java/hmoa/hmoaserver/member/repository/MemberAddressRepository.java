package hmoa.hmoaserver.member.repository;

import hmoa.hmoaserver.member.domain.MemberAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberAddressRepository extends JpaRepository<MemberAddress, Long> {
}
