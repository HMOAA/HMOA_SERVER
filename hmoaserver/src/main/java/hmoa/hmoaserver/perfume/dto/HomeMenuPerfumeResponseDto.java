package hmoa.hmoaserver.perfume.dto;

import hmoa.hmoaserver.perfume.domain.Perfume;
import lombok.Data;

@Data
public class HomeMenuPerfumeResponseDto {
    private Long perfumeId;
    private String brandName;
    private String perfumeName;
    private String imgUrl;
    public HomeMenuPerfumeResponseDto(Perfume perfume){
        this.perfumeId = perfume.getId();
        this.brandName = perfume.getBrand().getBrandName();
        this.perfumeName = perfume.getKoreanName();
        this.imgUrl = perfume.getPerfumePhoto().getPhotoUrl();
    }
}
