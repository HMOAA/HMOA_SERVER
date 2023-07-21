package hmoa.hmoaserver.brand.dto;

import hmoa.hmoaserver.brand.domain.Brand;
import lombok.Data;

@Data
public class BrandSaveRequestDto {

    private String brandName;
    private String englishName;

    public Brand toEntity(int consonant) {
        return Brand.builder()
                .brandName(brandName)
                .englishName(englishName)
                .consonant(consonant)
                .build();
    }
}
