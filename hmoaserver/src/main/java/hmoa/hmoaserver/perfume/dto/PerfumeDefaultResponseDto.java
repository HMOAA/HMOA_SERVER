package hmoa.hmoaserver.perfume.dto;

import hmoa.hmoaserver.perfume.domain.Perfume;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PerfumeDefaultResponseDto {

    private Long perfumeId;
    private String brandName;
    private String perfumeName;
    private String perfumeInfo;
    private String perfumeImageUrl;

    public PerfumeDefaultResponseDto(Perfume perfume) {
        this.perfumeId = perfume.getId();
        this.brandName = perfume.getBrand().getBrandName();
        this.perfumeName = perfume.getKoreanName();
        this.perfumeInfo = perfume.getPerfumeInfo();
        this.perfumeImageUrl = perfume.getPerfumePhoto().getPhotoUrl();
    }
}
