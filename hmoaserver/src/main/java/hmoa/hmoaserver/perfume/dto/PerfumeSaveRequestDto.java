package hmoa.hmoaserver.perfume.dto;

import hmoa.hmoaserver.brand.domain.Brand;
import hmoa.hmoaserver.perfume.domain.Perfume;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@ToString
@Data
@ApiModel
public class PerfumeSaveRequestDto {
    @ApiModelProperty(position = 0)
    private String aBrandName;
    @ApiModelProperty(position = 1)
    private String bKoreanName;
    @ApiModelProperty(position = 2)
    private String cEnglishName;
    @ApiModelProperty(position = 3,example = "15000")
    private int dPrice;
    @ApiModelProperty(position = 4)
    private List<Integer> volume;
    @ApiModelProperty(position = 5,example = "1")
    private int priceVolume;
    @ApiModelProperty(position = 6,example = "0")
    private int sortType;
    @ApiModelProperty(position = 7)
    private String eTopNote;
    @ApiModelProperty(position = 8)
    private String fHeartNote;
    @ApiModelProperty(position = 9)
    private String gBaseNote;
    @ApiModelProperty(position = 10)
    private List<String> hSingleNote;
    @ApiModelProperty
    private List<Integer> notePhotos;

    public Perfume toEntity(Brand brand) {
        if(sortType==0) {
            return Perfume.builder()
                    .koreanName(bKoreanName)
                    .englishName(cEnglishName)
                    .volume(volume)
                    .topNote(eTopNote)
                    .heartNote(fHeartNote)
                    .baseNote(gBaseNote)
                    .priceVolume(priceVolume)
                    .price(dPrice)
                    .sortType(sortType)
                    .searchName(removeSpace(bKoreanName))
                    .brand(brand)
                    .notePhotos(notePhotos)
                    .build();
        }else{
            return Perfume.builder()
                    .koreanName(bKoreanName)
                    .englishName(cEnglishName)
                    .volume(volume)
                    .topNote(eTopNote)
                    .singleNote(hSingleNote)
                    .price(dPrice)
                    .sortType(sortType)
                    .searchName(removeSpace(bKoreanName))
                    .brand(brand)
                    .notePhotos(notePhotos)
                    .build();
        }
    }
    private String removeSpace(String str){
        String result = str.replaceAll(" ","");
        return result;
    }
}
