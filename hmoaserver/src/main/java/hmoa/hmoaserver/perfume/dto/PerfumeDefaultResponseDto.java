package hmoa.hmoaserver.perfume.dto;

import hmoa.hmoaserver.perfume.domain.Perfume;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PerfumeDefaultResponseDto {
    private Long perfumeId;
    private String brandName;
    private String koreanName;
    private String englishName;
    private String perfumeImageUrl;
    private int price;

    public PerfumeDefaultResponseDto(Perfume perfume) {
        this.perfumeId = perfume.getId();
        this.brandName = perfume.getBrand().getBrandName();
        this.koreanName = perfume.getKoreanName();
        this.englishName = perfume.getEnglishName();
        this.perfumeImageUrl=perfume.getPerfumePhoto().getPhotoUrl();
        this.price = perfume.getPrice();
    }
}
