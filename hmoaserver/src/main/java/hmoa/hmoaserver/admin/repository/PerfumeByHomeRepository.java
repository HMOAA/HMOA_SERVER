package hmoa.hmoaserver.admin.repository;

import hmoa.hmoaserver.admin.domain.HomeMenu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerfumeByHomeRepository extends JpaRepository<HomeMenu, Long> {
}
