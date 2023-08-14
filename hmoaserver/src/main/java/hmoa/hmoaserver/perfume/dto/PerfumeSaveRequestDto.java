package hmoa.hmoaserver.perfume.dto;

import hmoa.hmoaserver.brand.domain.Brand;
import hmoa.hmoaserver.perfume.domain.Perfume;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class PerfumeSaveRequestDto {
    @ApiModelProperty(position = 0)
    private String brandName;
    @ApiModelProperty(position = 1)
    private String koreanName;
    @ApiModelProperty(position = 2)
    private String englishName;
    @ApiModelProperty(position = 3,example = "15000")
    private int price;
    @ApiModelProperty(position = 4)
    private List<Integer> volume;
    @ApiModelProperty(position = 5,example = "1")
    private int priceVolume;
    @ApiModelProperty(position = 6)
    private String topNote;
    @ApiModelProperty(position = 7)
    private String heartNote;
    @ApiModelProperty(position = 8)
    private String baseNote;

    public Perfume toEntity(Brand brand) {
        return Perfume.builder()
                .koreanName(koreanName)
                .englishName(englishName)
                .volume(volume)
                .topNote(topNote)
                .heartNote(heartNote)
                .baseNote(baseNote)
                .priceVolume(priceVolume)
                .price(price)
                .brand(brand)
                .build();
    }
}
