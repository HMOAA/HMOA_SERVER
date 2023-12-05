package hmoa.hmoaserver.homemenu.dto;

import hmoa.hmoaserver.perfume.domain.Perfume;
import lombok.Data;

@Data
public class HomeMenuBrandResponseDto {
    private Long perfumeId;
    private String brandName;
    private String imgUrl;

    public HomeMenuBrandResponseDto(Perfume perfume) {
        this.perfumeId = perfume.getId();
        this.brandName = perfume.getBrand().getEnglishName();
        this.imgUrl = perfume.getPerfumePhoto().getPhotoUrl();
    }
}
