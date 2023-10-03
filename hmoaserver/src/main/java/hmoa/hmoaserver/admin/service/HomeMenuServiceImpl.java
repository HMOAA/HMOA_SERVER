package hmoa.hmoaserver.admin.service;

import hmoa.hmoaserver.admin.domain.HomeMenu;
import hmoa.hmoaserver.admin.dto.PerfumeByHomeSaveRequestDto;
import hmoa.hmoaserver.admin.repository.PerfumeByHomeRepository;
import hmoa.hmoaserver.exception.Code;
import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.member.domain.Role;
import hmoa.hmoaserver.perfume.domain.Perfume;
import hmoa.hmoaserver.perfume.service.PerfumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class HomeMenuServiceImpl implements HomeMenuService {
    private final PerfumeByHomeRepository perfumeByHomeRepository;
    private final PerfumeService perfumeService;

    @Override
    public HomeMenu save(PerfumeByHomeSaveRequestDto dto) {
        try {
            return perfumeByHomeRepository.save(dto.toEntity());
        }catch (RuntimeException e){
            throw new CustomException(null,Code.SERVER_ERROR);
        }
    }

    @Override
    public HomeMenu addPerfumeForHomeMenu(Long perfumeId, Long homeId) {
        Perfume perfume = perfumeService.findById(perfumeId);
        HomeMenu homeMenu = findHomeById(homeId);
        perfume.setHomeMenu(homeMenu);
        return homeMenu;
    }

    @Override
    public void deleteHomeMenu(Long perfumeId) {
        Perfume perfume = perfumeService.findById(perfumeId);
        perfume.deleteHomeMenu();
    }

    private HomeMenu findHomeById(Long homeId){
        return perfumeByHomeRepository.findById(homeId).orElseThrow(()-> new CustomException(null,Code.HOMEMENU_NOT_FOUND));
    }
}
