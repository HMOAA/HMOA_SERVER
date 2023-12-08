package hmoa.hmoaserver.homemenu.repository;

import hmoa.hmoaserver.homemenu.domain.HomeMenu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HomeMenuRepository extends JpaRepository<HomeMenu, Long> {
}
