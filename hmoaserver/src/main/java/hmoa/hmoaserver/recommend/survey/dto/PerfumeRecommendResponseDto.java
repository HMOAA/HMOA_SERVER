package hmoa.hmoaserver.recommend.survey.dto;

import hmoa.hmoaserver.perfume.domain.Perfume;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class PerfumeRecommendResponseDto {

    private Long perfumeId;
    private String perfumeName;
    private String perfumeEnglishName;
    private int price;

    public PerfumeRecommendResponseDto(final Perfume perfume) {
        this.perfumeId = perfume.getId();
        this.perfumeName = perfume.getKoreanName();
        this.perfumeEnglishName = perfume.getEnglishName();
        this.price = perfume.getPrice();
    }
}
