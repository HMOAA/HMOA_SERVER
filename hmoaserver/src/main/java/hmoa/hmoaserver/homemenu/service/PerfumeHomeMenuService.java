package hmoa.hmoaserver.homemenu.service;

import hmoa.hmoaserver.homemenu.domain.HomeMenu;
import hmoa.hmoaserver.homemenu.domain.PerfumeHomeMenu;
import hmoa.hmoaserver.perfume.domain.Perfume;

import java.util.List;

public interface PerfumeHomeMenuService {
    PerfumeHomeMenu save(HomeMenu homeMenu, Perfume perfume);
    void reset(HomeMenu homeMenu);
    List<PerfumeHomeMenu> getPerfumeHomeMenus(HomeMenu homeMenu);
}
