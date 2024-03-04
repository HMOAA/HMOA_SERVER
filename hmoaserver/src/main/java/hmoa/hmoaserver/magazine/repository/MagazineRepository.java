package hmoa.hmoaserver.magazine.repository;

import hmoa.hmoaserver.magazine.domain.Magazine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MagazineRepository extends JpaRepository<Magazine, Long> {
}
