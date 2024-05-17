package hmoa.hmoaserver.perfume.dto;

import hmoa.hmoaserver.perfume.domain.Perfume;
import hmoa.hmoaserver.perfume.review.dto.PerfumeReviewResponseDto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PerfumeDetailResponseDto {

    private Long perfumeId;
    private int heartNum;
    private Long brandId;
    private String brandName;
    private String brandEnglishName;
    private String brandImgUrl;
    private String koreanName;
    private String englishName;
    private String perfumeImageUrl;
    private int price;
    private List<Integer> volume;
    private int priceVolume;
    private String topNote;
    private String heartNote;
    private String baseNote;
    private List<Integer> notePhotos;
    private List<String> singleNote;
    private int sortType;
    private boolean isLiked = false;
    private PerfumeReviewResponseDto review;

    public PerfumeDetailResponseDto(Perfume perfume, boolean isLiked, PerfumeReviewResponseDto review) {
        this.perfumeId = perfume.getId();
        this.heartNum = perfume.getHeartCount();
        this.perfumeImageUrl = perfume.getPerfumePhoto().getPhotoUrl();
        this.brandId = perfume.getBrand().getId();
        this.brandEnglishName = perfume.getBrand().getEnglishName();
        this.brandImgUrl = perfume.getBrand().getBrandPhoto().getPhotoUrl();
        this.brandName = perfume.getBrand().getBrandName();
        this.koreanName = perfume.getKoreanName();
        this.englishName = perfume.getEnglishName();
        this.volume = perfume.getVolume();
        this.isLiked = isLiked;
        this.notePhotos = perfume.getNotePhotos();
        this.topNote = perfume.getTopNote();
        this.heartNote = perfume.getHeartNote();
        this.baseNote = perfume.getBaseNote();
        this.singleNote = perfume.getSingleNote();
        this.priceVolume=perfume.getPriceVolume();
        this.price = perfume.getPrice();
        this.sortType = perfume.getSortType();
        this.review = review;
    }
}
