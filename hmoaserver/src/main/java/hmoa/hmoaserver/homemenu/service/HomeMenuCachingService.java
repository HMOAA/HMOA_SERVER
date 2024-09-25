package hmoa.hmoaserver.homemenu.service;

import hmoa.hmoaserver.config.setting.CacheName;
import hmoa.hmoaserver.homemenu.domain.HomeMenu;
import hmoa.hmoaserver.homemenu.domain.PerfumeHomeMenu;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HomeMenuCachingService {
    private final HomeMenuService homeMenuService;
    private final PerfumeHomeMenuService perfumeHomeMenuService;

    @Cacheable(cacheNames = CacheName.MAIN_PAGE, key = "#homeMenuId")
    public HomeMenu getHomeMenu(Long homeMenuId) {
        return homeMenuService.getHomeMenu(homeMenuId);
    }

    @Cacheable(cacheNames = CacheName.MAIN_PAGE_PERFUME, key = "#homeMenu.getId()")
    public List<PerfumeHomeMenu> getPerfumeHomeMenus(HomeMenu homeMenu) {
        return perfumeHomeMenuService.getPerfumeHomeMenus(homeMenu);
    }
}
