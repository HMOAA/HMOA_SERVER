package hmoa.hmoaserver.perfume.service;

import hmoa.hmoaserver.brand.domain.Brand;
import hmoa.hmoaserver.brand.repository.BrandRepository;
import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.perfume.domain.Perfume;
import hmoa.hmoaserver.perfume.dto.PerfumeNewRequestDto;
import hmoa.hmoaserver.perfume.dto.PerfumeSaveRequestDto;
import hmoa.hmoaserver.perfume.repository.PerfumeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static hmoa.hmoaserver.exception.Code.*;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class PerfumeService {

    private final PerfumeRepository perfumeRepository;
    private final BrandRepository brandRepository;
    private static final Pageable DEFAULT_PAGEABLE = PageRequest.of(0, 10);

    public Perfume save(PerfumeSaveRequestDto requestDto) {
        Brand brand = brandRepository.findByBrandName(requestDto.getABrandName())
                        .orElseThrow(() -> new CustomException(null, BRAND_NOT_FOUND));

        return perfumeRepository.save(requestDto.toEntity(brand));
    }

    public Perfume newSave(PerfumeNewRequestDto dto) {
        Brand brand = brandRepository.findByBrandName(dto.getBrandName())
                .orElseThrow(() -> new CustomException(null, BRAND_NOT_FOUND));
        int sortType = 0;
        List<Integer> notePhoto = new ArrayList<>(), volume;
        if (!(dto.getNotePhotos() == null || dto.getNotePhotos().equals(""))) {
            String[] notePhotos = dto.getNotePhotos().split(",");
            sortType = notePhotos.length;
            notePhoto = Arrays.stream(notePhotos).map(Integer::parseInt).collect(Collectors.toList());
        }
        String[] volumes = dto.getVolumes().split(",");
        volume = Arrays.stream(volumes).map(Integer::parseInt).collect(Collectors.toList());
        int priceVolume = 0;
        for (int i = 0; i < volume.size(); i++) {
            if (dto.getPriceVolume() == volume.get(i)) {
                priceVolume = i + 1;
            }
        }
        return perfumeRepository.save(dto.toEntity(brand, sortType, volume, notePhoto, priceVolume));
    }
    /**
     *  향수 단건 조회
     */
    public Perfume findById(Long perfumeId) {
        return perfumeRepository.findById(perfumeId)
                .orElseThrow(() -> new CustomException(null, PERFUME_NOT_FOUND));
    }

    /**
     *  향수 조회(최신순) 보류
     */
    public Page<Perfume> findUpdatePerfumesByBrand(Long brandId, int pageNum) {
        try {
            Page<Perfume> foundPerfumes =
                    perfumeRepository.findAllByBrandIdOrderByCreatedAtDescIdAsc(
                            brandId,
                            PageRequest.of(pageNum, 6)
                    );
            return foundPerfumes;
        } catch (DataAccessException | ConstraintViolationException e) {
            throw new CustomException(null, SERVER_ERROR);
        }
    }

    /**
     *  향수 조회(문자열순)
     */
    public Page<Perfume> findPerfumesByBrand(Long brandId, int pageNum) {
        try {
            Page<Perfume> foundPerfumes =
                    perfumeRepository.findAllByBrandIdOrderByKoreanNameAscIdAsc(
                            brandId,
                            PageRequest.of(pageNum, 6)
                    );
            return foundPerfumes;
        } catch (DataAccessException | ConstraintViolationException e) {
            throw new CustomException(null, SERVER_ERROR);
        }
    }

    /**
     *  향수 조회(좋아요순)
     */
    public Page<Perfume> findTopPerfumesByBrand(Long brandId, int pageNum) {
        try {
            Page<Perfume> foundPerfumes =
                    perfumeRepository.findAllByBrandIdOrderByHeartCountDescIdAsc(
                            brandId,
                            PageRequest.of(pageNum, 6)
                    );
            return foundPerfumes;
        } catch (DataAccessException | ConstraintViolationException e) {
            throw new CustomException(null, SERVER_ERROR);
        }
    }

    public Page<Perfume> findSameBrandPerfume(Long brandId, Long perfumeId) {
        try {
            return perfumeRepository.findAllByBrandIdAndIdNot(brandId, perfumeId, PageRequest.of(0, 6));
        } catch (DataAccessException | ConstraintViolationException e) {
            throw new CustomException(null, SERVER_ERROR);
        }
    }

    public boolean isExistPerfume(PerfumeSaveRequestDto dto) {
        Brand brand = brandRepository.findByBrandName(dto.getABrandName())
                .orElseThrow(() -> new CustomException(null, BRAND_NOT_FOUND));
        return perfumeRepository.findByBrandIdAndKoreanName(brand.getId(), dto.getBKoreanName()).isPresent();
    }

    public void saveRelase(Perfume perfume, LocalDate localDate) {
        perfume.setRelaseDate(localDate);
    }

    public Page<Perfume> findRecentPerfumes() {
        return perfumeRepository.findAllByOrderByReleaseDateDescIdAsc(DEFAULT_PAGEABLE);
    }

    public Perfume findPerfumeName(String name) {
        return perfumeRepository.findByKoreanName(name).orElseThrow(() -> new CustomException(null, PERFUME_NOT_FOUND));
    }
}
