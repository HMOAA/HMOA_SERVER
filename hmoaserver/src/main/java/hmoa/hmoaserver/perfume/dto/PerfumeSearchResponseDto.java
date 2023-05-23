package hmoa.hmoaserver.perfume.dto;


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
    private boolean heart;

    public PerfumeSearchResponseDto(Perfume perfume) {
        this.perfumeId = perfume.getId();
        this.brandName = perfume.getBrand().getBrandName();
        this.perfumeName = perfume.getKoreanName();
        this.perfumeImageUrl = perfume.getPerfumePhoto().getPhotoUrl();
        this.heart=false;
    }
}