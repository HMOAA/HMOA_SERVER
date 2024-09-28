package hmoa.hmoaserver.perfume.service;

import hmoa.hmoaserver.config.setting.CacheName;
import hmoa.hmoaserver.perfume.domain.Perfume;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PerfumeCachingService {

    private final PerfumeService perfumeService;

    @Cacheable(cacheNames = CacheName.PERFUME, key = "#perfumeId")
    public Perfume getPerfumeById(final Long perfumeId) {
        return perfumeService.findById(perfumeId);
    }
}
