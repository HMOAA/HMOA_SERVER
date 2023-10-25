package hmoa.hmoaserver.perfumer.repository;

import hmoa.hmoaserver.perfumer.domain.Perfumer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerfumerRepository extends JpaRepository<Perfumer, Long> {
}
