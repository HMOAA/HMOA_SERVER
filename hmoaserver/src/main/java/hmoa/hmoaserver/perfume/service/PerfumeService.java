package hmoa.hmoaserver.perfume.service;

import hmoa.hmoaserver.brand.domain.Brand;
import hmoa.hmoaserver.brand.repository.BrandRepository;
import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.perfume.domain.Perfume;
import hmoa.hmoaserver.perfume.dto.PerfumeNewRequestDto;
import hmoa.hmoaserver.perfume.dto.PerfumeRecommendation;
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

    private static final List<String> EXCLUDED_KEYWORD = List.of("단종");

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

    public void deleteExcludedPerfumes() {
        List<Perfume> perfumes;

        for (String keyword : EXCLUDED_KEYWORD) {
            perfumes = perfumeRepository.findByKoreanNameContaining(keyword);
            perfumes.forEach(perfumeRepository::delete);
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

    /**
     * 향수 추천
     */
    @Transactional(readOnly = true)
    public List<PerfumeRecommendation> recommendPerfumes(int minPrice, int maxPrice, List<String> notes) {
        List<Perfume> affordablePerfumes = perfumeRepository.findAllAffordablePerfumes(minPrice, maxPrice);

        return affordablePerfumes.stream()
                .map(perfume -> {
                    int score = calculateScore(perfume, notes);
                    return new PerfumeRecommendation(perfume, score);
                })
                .sorted((r1, r2) -> Integer.compare(r2.getScore(), r1.getScore()))
                .limit(3) // 상위 5개만 반환
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PerfumeRecommendation> recommendPerfumesIncludePrice(int minPrice, int maxPrice, List<String> notes) {
        List<Perfume> perfumes = perfumeRepository.findAll();

        return perfumes.stream()
                .map(perfume -> {
                    int score = calculateScore(perfume, notes, minPrice, maxPrice);
                    return new PerfumeRecommendation(perfume, score);
                })
                .sorted((p1, p2) -> Integer.compare(p2.getScore(), p1.getScore()))
                .limit(3)
                .collect(Collectors.toList());
    }

    /**
     * 향수 추천 점수 계산
     */
    public int calculateScore(Perfume perfume, List<String> notes) {
        int score = 0;

        return addContainNotePoint(perfume, notes, score);
    }

    /**
     * 향수 추천 점수 계산 가격 포함
     */
    public int calculateScore(Perfume perfume, List<String> notes, int minPrice, int maxPrice) {
        int score = 0;

        if (perfume.getPrice() > minPrice && perfume.getPrice() <= maxPrice) {
            score += 9;
        }

        return addContainNotePoint(perfume, notes, score);
    }

    private static int addContainNotePoint(Perfume perfume, List<String> notes, int score) {
        for (String note : notes) {
            if (perfume.getHeartNote() != null && containsExactMatch(perfume.getHeartNote(), note)) score += 10;
            if (perfume.getTopNote() != null && containsExactMatch(perfume.getTopNote(), note)) score += 10;
            if (perfume.getBaseNote() != null && containsExactMatch(perfume.getBaseNote(), note)) score += 10;
        }

        return score;
    }

    // '핑크로즈'가 '로즈'에 포함되지 않도록 정확히 일치하는 경우에만 true 값을 반환하는 메소드
    private static boolean containsExactMatch(String notes, String searchNote) {
        List<String> values = Arrays.asList(notes.trim().split(",\\s*"));
        return values.contains(searchNote);
    }
}
