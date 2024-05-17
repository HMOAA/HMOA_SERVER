package hmoa.hmoaserver.search.dto;


import hmoa.hmoaserver.perfume.domain.Perfume;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PerfumeSearchResponseDto {
    private Long perfumeId;
    private String brandName;
    private String perfumeName;
    private String perfumeImageUrl;
    private boolean heart = false;

    public PerfumeSearchResponseDto(Perfume perfume, boolean heart) {
        this.perfumeId = perfume.getId();
        this.brandName = perfume.getBrand().getBrandName();
        this.perfumeName = perfume.getKoreanName();
        this.perfumeImageUrl = perfume.getPerfumePhoto().getPhotoUrl();
        this.heart = heart;
    }
}
