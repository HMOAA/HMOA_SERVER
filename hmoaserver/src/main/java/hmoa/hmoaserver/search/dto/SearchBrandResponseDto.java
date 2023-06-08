package hmoa.hmoaserver.search.dto;

import hmoa.hmoaserver.brand.domain.Brand;
import hmoa.hmoaserver.search.service.UnicodeService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SearchBrandResponseDto {
    private Long brandId;
    private String brandName;
    private String brandImageUrl;
    private int consonant;

    public SearchBrandResponseDto(Brand brand, int num){
        this.brandId=brand.getId();
        this.brandName=brand.getBrandName();
        if(brand.getBrandPhoto()!=null){
            this.brandImageUrl=brand.getBrandPhoto().getPhotoUrl();
        }else {
            this.brandImageUrl="empty";
        }
        this.consonant=num;
    }
}
