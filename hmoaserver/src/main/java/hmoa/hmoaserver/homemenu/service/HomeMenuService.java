package hmoa.hmoaserver.homemenu.service;

import hmoa.hmoaserver.homemenu.domain.HomeMenu;
import hmoa.hmoaserver.homemenu.dto.HomeMenuSaveRequestDto;
import hmoa.hmoaserver.perfume.domain.Perfume;

import java.util.List;

public interface HomeMenuService {
    HomeMenu save(HomeMenuSaveRequestDto dto);
    HomeMenu addPerfumeForHomeMenu(Long perfumeId, Long homeId);
    void deleteHomeMenu(Long perfumeId);
    List<Perfume> findPerfumesByHomeMenu(HomeMenu homeMenu);
    HomeMenu findHomeMenuById(Long homeId);
}
