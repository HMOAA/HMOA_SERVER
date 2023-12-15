package hmoa.hmoaserver.homemenu.service;

import hmoa.hmoaserver.homemenu.domain.HomeMenu;
import hmoa.hmoaserver.homemenu.dto.HomeMenuSaveRequestDto;
import hmoa.hmoaserver.homemenu.repository.HomeMenuRepository;
import hmoa.hmoaserver.exception.Code;
import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.perfume.domain.Perfume;
import hmoa.hmoaserver.perfume.service.PerfumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class HomeMenuServiceImpl implements HomeMenuService {
    private final HomeMenuRepository homeMenuRepository;
    private final PerfumeService perfumeService;

    @Override
    public HomeMenu save(HomeMenuSaveRequestDto dto) {
        try {
            return homeMenuRepository.save(dto.toEntity());
        }catch (RuntimeException e){
            throw new CustomException(null,Code.SERVER_ERROR);
        }
    }

    @Override
    public HomeMenu addPerfumeForHomeMenu(Long perfumeId, Long homeId) {
        Perfume perfume = perfumeService.findById(perfumeId);
        HomeMenu homeMenu = findHomeMenuById(homeId);
        perfume.setHomeMenu(homeMenu);
        return homeMenu;
    }

    @Override
    public void deleteHomeMenu(Long perfumeId) {
        Perfume perfume = perfumeService.findById(perfumeId);
        perfume.deleteHomeMenu();
    }

    @Override
    public List<Perfume> findPerfumesByHomeMenu(HomeMenu homeMenu) {
        return homeMenu.getPerfumeList();
    }

    @Override
    public HomeMenu findHomeMenuById(Long homeId) {
        return homeMenuRepository.findById(homeId).orElseThrow(() -> new CustomException(null, Code.HOMEMENU_NOT_FOUND));
    }

    @Override
    public void resetHomeMenu(HomeMenu homeMenu) {
        List<Perfume> perfumes = homeMenu.getPerfumeList();
        perfumes.forEach(Perfume::deleteHomeMenu);
    }

    @Override
    public void modifyHomeMenu(HomeMenu homeMenu, String title) {
        homeMenu.updateTitle(title);
    }
}
