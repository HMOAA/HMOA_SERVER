package hmoa.hmoaserver.homemenu.repository;

import hmoa.hmoaserver.homemenu.domain.HomeMenu;
import hmoa.hmoaserver.homemenu.domain.PerfumeHomeMenu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PerfumeHomeMenuRepository extends JpaRepository<PerfumeHomeMenu, Long> {
    List<PerfumeHomeMenu> findAllByHomeMenu(HomeMenu homeMenu);
}
