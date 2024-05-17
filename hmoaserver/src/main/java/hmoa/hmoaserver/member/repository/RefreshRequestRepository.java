package hmoa.hmoaserver.member.repository;

import hmoa.hmoaserver.member.domain.RefreshRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshRequestRepository extends JpaRepository<RefreshRequest, Long> {
}
