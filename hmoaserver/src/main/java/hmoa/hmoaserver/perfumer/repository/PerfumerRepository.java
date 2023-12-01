package hmoa.hmoaserver.perfumer.repository;

import hmoa.hmoaserver.perfumer.domain.Perfumer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerfumerRepository extends JpaRepository<Perfumer, Long> {
    Page<Perfumer> findAll(Pageable pageable);

    Page<Perfumer> findByTitleContainingOrSubtitleContainingOrderByCreatedAtDesc(
            String title, String subtitle, Pageable pageable
    );
}

