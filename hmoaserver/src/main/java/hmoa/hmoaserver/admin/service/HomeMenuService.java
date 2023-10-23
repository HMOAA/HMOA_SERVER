package hmoa.hmoaserver.admin.service;

import hmoa.hmoaserver.admin.domain.HomeMenu;
import hmoa.hmoaserver.admin.dto.HomeMenuSaveRequestDto;
import hmoa.hmoaserver.perfume.domain.Perfume;

import java.util.List;

public interface HomeMenuService {
    HomeMenu save(HomeMenuSaveRequestDto dto);

    HomeMenu addPerfumeForHomeMenu(Long perfumeId, Long homeId);
    void deleteHomeMenu(Long perfumeId);

    List<Perfume> findPerfumesByHomeMenu(Long homeId);
}
