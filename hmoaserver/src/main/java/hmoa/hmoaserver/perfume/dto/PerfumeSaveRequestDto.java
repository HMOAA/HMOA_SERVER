package hmoa.hmoaserver.perfume.dto;

import hmoa.hmoaserver.brand.domain.Brand;
import hmoa.hmoaserver.perfume.domain.Perfume;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;
@ToString
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
                .searchName(removeSpace(koreanName))
                .brand(brand)
                .build();
    }
    private String removeSpace(String str){
        String result = str.replaceAll(" ","");
        return result;
    }
}
