package hmoa.hmoaserver.perfume.dto;

import hmoa.hmoaserver.perfume.domain.Perfume;
import lombok.Data;

@Data
public class PerfumeSimilarResponseDto {
    private String brandName;
    private String perfumeImgUrl;
    private String perfumeName;
    public PerfumeSimilarResponseDto(Perfume perfume){
        this.brandName=perfume.getBrand().getBrandName();
        this.perfumeName=perfume.getKoreanName();
        this.perfumeImgUrl=perfume.getPerfumePhoto().getPhotoUrl();
    }
}
