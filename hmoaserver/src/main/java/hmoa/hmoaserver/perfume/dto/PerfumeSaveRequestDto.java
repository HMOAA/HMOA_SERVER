package hmoa.hmoaserver.perfume.dto;

import hmoa.hmoaserver.brand.domain.Brand;
import hmoa.hmoaserver.perfume.domain.Perfume;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class PerfumeSaveRequestDto {

    private String koreanName;
    private String englishName;
    private String perfumeInfo;
    private Long price;
    private String brandName;

    public Perfume toEntity(Brand brand) {
        return Perfume.builder()
                .koreanName(koreanName)
                .englishName(englishName)
                .perfumeInfo(perfumeInfo)
                .price(price)
                .brand(brand)
                .build();
    }
}
