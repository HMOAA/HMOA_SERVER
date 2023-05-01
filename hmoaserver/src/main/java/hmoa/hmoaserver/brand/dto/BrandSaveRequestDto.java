package hmoa.hmoaserver.brand.dto;

import hmoa.hmoaserver.brand.domain.Brand;
import lombok.Data;

@Data
public class BrandSaveRequestDto {

    private String brandName;
    private String englishName;

    public Brand toEntity() {
        return Brand.builder()
                .brandName(brandName)
                .englishName(englishName)
                .build();
    }
}
