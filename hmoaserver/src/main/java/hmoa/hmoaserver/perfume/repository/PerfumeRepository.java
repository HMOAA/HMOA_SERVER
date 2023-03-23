package hmoa.hmoaserver.perfume.repository;

import hmoa.hmoaserver.perfume.domain.Perfume;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerfumeRepository extends JpaRepository<Perfume, Long> {
}
