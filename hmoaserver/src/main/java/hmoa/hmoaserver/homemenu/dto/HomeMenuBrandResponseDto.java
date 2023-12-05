package hmoa.hmoaserver.homemenu.dto;

import hmoa.hmoaserver.brand.domain.Brand;
import lombok.Data;

@Data
public class HomeMenuBrandResponseDto {
    private Long brandId;
    private String brandName;
    private String imgUrl;

    public HomeMenuBrandResponseDto(Brand brand) {
        this.brandId = brand.getId();
        this.brandName = brand.getEnglishName();
        this.imgUrl = brand.getBrandPhoto().getPhotoUrl();
    }
}
