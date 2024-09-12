package hmoa.hmoaserver.homemenu.repository;

import hmoa.hmoaserver.homemenu.domain.HomeMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface HomeMenuRepository extends JpaRepository<HomeMenu, Long> {

    @Query("SELECT hm FROM HomeMenu hm LEFT JOIN FETCH hm.perfumeHomeMenus WHERE hm.id = :homeMenuId")
    Optional<HomeMenu> findByHomeMenuId(@Param("homeMenuId") Long homeMenuId);
}
