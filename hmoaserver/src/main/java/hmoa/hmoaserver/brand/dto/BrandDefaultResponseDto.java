package hmoa.hmoaserver.brand.dto;

import hmoa.hmoaserver.brand.domain.Brand;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BrandDefaultResponseDto {

    private Long brandId;
    private String brandName;
    private String brandImageUrl;

    public BrandDefaultResponseDto(Brand brand) {
        this.brandId = brand.getId();
        this.brandName = brand.getBrandName();
        if(brand.getBrandPhoto()!=null){
            this.brandImageUrl = brand.getBrandPhoto().getPhotoUrl();
        }
    }
}
