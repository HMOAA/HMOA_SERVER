package hmoa.hmoaserver.homemenu.service;

import hmoa.hmoaserver.homemenu.domain.HomeMenu;
import hmoa.hmoaserver.homemenu.dto.HomeMenuSaveRequestDto;

public interface HomeMenuService {
    HomeMenu save(HomeMenuSaveRequestDto dto);
    void modifyHomeMenu(HomeMenu homeMenu, String title);
    HomeMenu getHomeMenu(Long homeId);
}
