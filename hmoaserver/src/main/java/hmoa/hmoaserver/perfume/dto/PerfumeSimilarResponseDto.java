package hmoa.hmoaserver.perfume.dto;

import hmoa.hmoaserver.perfume.domain.Perfume;
import lombok.Data;

@Data
public class PerfumeSimilarResponseDto {
    private Long brandId;
    private String brandName;
    private Long perfumeId;
    private String perfumeImgUrl;
    private String perfumeName;
    public PerfumeSimilarResponseDto(Perfume perfume){
        this.brandId = perfume.getBrand().getId();
        this.perfumeId = perfume.getId();
        this.brandName=perfume.getBrand().getBrandName();
        this.perfumeName=perfume.getKoreanName();
        this.perfumeImgUrl=perfume.getPerfumePhoto().getPhotoUrl();
    }
}
