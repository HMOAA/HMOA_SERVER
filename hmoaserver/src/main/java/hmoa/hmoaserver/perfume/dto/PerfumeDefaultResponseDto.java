package hmoa.hmoaserver.perfume.dto;

import hmoa.hmoaserver.perfume.domain.Perfume;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PerfumeDefaultResponseDto {

    private Long perfumeId;
    private String brandName;
    private String koreanName;
    private String englishName;
    private String perfumeInfo;
    private Long price;
    private String perfumeImageUrl;

    public PerfumeDefaultResponseDto(Perfume perfume) {
        this.perfumeId = perfume.getId();
        this.brandName = perfume.getBrand().getBrandName();
        this.koreanName = perfume.getKoreanName();
        this.englishName = perfume.getEnglishName();
        this.price = perfume.getPrice();
        this.perfumeInfo = perfume.getPerfumeInfo();
        this.perfumeImageUrl = perfume.getPerfumePhoto().getPhotoUrl();
    }
}
