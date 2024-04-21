package hmoa.hmoaserver.perfumer.repository;

import hmoa.hmoaserver.perfumer.domain.Perfumer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PerfumerRepository extends JpaRepository<Perfumer, Long> {
    Page<Perfumer> findAll(Pageable pageable);

    Page<Perfumer> findByTitleContainingOrSubTitleContainingOrderByCreatedAtDesc(
            String title, String subtitle, Pageable pageable
    );

    Page<Perfumer> findAllByOrderByIdDesc(Pageable pageable);

    @Query("SELECT p " +
            "FROM Perfumer p " +
            "WHERE p.id < ?1 " +
            "ORDER BY p.createdAt DESC, p.id Desc")
    Page<Perfumer> findPerfumerNextPage(Long lastCommentId, PageRequest pageRequest);
}

