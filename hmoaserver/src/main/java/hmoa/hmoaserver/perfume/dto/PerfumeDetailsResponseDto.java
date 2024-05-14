package hmoa.hmoaserver.perfume.dto;

import hmoa.hmoaserver.perfume.domain.Perfume;
import hmoa.hmoaserver.perfume.review.dto.PerfumeReviewResponseDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PerfumeDetailsResponseDto {
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
    private List<Integer> notePhotos;
    private int sortType;
    private boolean isLiked = false;
    private PerfumeReviewResponseDto review;

    public PerfumeDetailsResponseDto(Perfume perfume, boolean isLiked, PerfumeReviewResponseDto review) {
        this.perfumeId = perfume.getId();
        this.heartNum = perfume.getHeartCount();
        this.brandId = perfume.getBrand().getId();
        this.brandName = perfume.getBrand().getBrandName();
        this.brandEnglishName = perfume.getBrand().getBrandName();
        this.brandImgUrl = perfume.getBrand().getBrandPhoto().getPhotoUrl();
        this.koreanName = perfume.getKoreanName();
        this.englishName = perfume.getEnglishName();
        this.perfumeImageUrl = perfume.getPerfumePhoto().getPhotoUrl();
        this.price = perfume.getPrice();
        this.volume = perfume.getVolume();
        this.priceVolume = perfume.getPriceVolume();
        this.notePhotos = perfume.getNotePhotos();
        this.sortType = perfume.getSortType();
        this.isLiked = isLiked;
        this.review = review;
    }
}
