package hmoa.hmoaserver.homemenu.service;

import hmoa.hmoaserver.exception.Code;
import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.homemenu.domain.HomeMenu;
import hmoa.hmoaserver.homemenu.domain.PerfumeHomeMenu;
import hmoa.hmoaserver.homemenu.repository.PerfumeHomeMenuRepository;
import hmoa.hmoaserver.perfume.domain.Perfume;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PerfumeHomeMenuServiceImpl implements PerfumeHomeMenuService {
    private final PerfumeHomeMenuRepository perfumeHomeMenuRepository;

    @Override
    public PerfumeHomeMenu save(HomeMenu homeMenu, Perfume perfume) {
        try {
            return perfumeHomeMenuRepository.save(PerfumeHomeMenu.builder()
                    .homeMenu(homeMenu)
                    .perfume(perfume)
                    .build());
        } catch (RuntimeException e) {
            throw new CustomException(null, Code.SERVER_ERROR);
        }
    }

    @Override
    public void reset(HomeMenu homeMenu) {
        List<PerfumeHomeMenu> perfumeHomeMenus = perfumeHomeMenuRepository.findAllByHomeMenu(homeMenu);
        perfumeHomeMenus.forEach(perfumeHomeMenuRepository::delete);
    }

    @Override
    public List<PerfumeHomeMenu> findPerfumeHomeMenuByHomeMenu(HomeMenu homeMenu) {
        return perfumeHomeMenuRepository.findAllByHomeMenu(homeMenu);
    }
}
