package hmoa.hmoaserver.magazine.repository;

import hmoa.hmoaserver.magazine.domain.Magazine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MagazineRepository extends JpaRepository<Magazine, Long> {
    Page<Magazine> findAllByOrderByCreatedAtDesc(Pageable pageable);

    @Query("SELECT m " +
            "FROM Magazine m " +
            "WHERE m.id < ?1 " +
            "ORDER BY m.createdAt DESC, m.id Desc")
    Page<Magazine> findMagazineNextPage(Long lastId, PageRequest pageRequest);
}
