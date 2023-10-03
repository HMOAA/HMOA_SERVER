package hmoa.hmoaserver.admin.service;

import hmoa.hmoaserver.admin.domain.HomeMenu;
import hmoa.hmoaserver.admin.dto.PerfumeByHomeSaveRequestDto;
import hmoa.hmoaserver.member.domain.Member;

public interface HomeMenuService {
    HomeMenu save(PerfumeByHomeSaveRequestDto dto);

    HomeMenu addPerfumeForHomeMenu(Long perfumeId, Long homeId);
    void deleteHomeMenu(Long perfumeId);
}
