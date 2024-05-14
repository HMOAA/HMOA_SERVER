package hmoa.hmoaserver.perfume.dto;

import hmoa.hmoaserver.brand.domain.Brand;
import hmoa.hmoaserver.perfume.domain.Perfume;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@ToString
@Data
public class PerfumeNewRequestDto {
    private String brandName;
    private String koreanName;
    private String englishName;
    private String price;
    private String volumes;
    private int priceVolume;
    private int sortType;
    private String topNote;
    private String heartNote;
    private String baseNote;
    private String notePhotos;

    public Perfume toEntity(Brand brand, int sortType, List<Integer> volumes, List<Integer> notePhotos, int priceVolume) {
        if (price.equals("가격미정")) {
            price = "0";
        }

        if (sortType == 0) {
            return Perfume.builder()
                    .koreanName(koreanName)
                    .englishName(englishName)
                    .volume(volumes)
                    .priceVolume(priceVolume)
                    .price(Integer.parseInt(price))
                    .sortType(sortType)
                    .searchName(removeSpace(koreanName))
                    .brand(brand)
                    .notePhotos(notePhotos)
                    .expected(true)
                    .build();
        } else if (sortType == 1) {
            return Perfume.builder()
                    .koreanName(koreanName)
                    .englishName(englishName)
                    .volume(volumes)
                    .topNote(topNote)
                    .priceVolume(priceVolume)
                    .price(Integer.parseInt(price))
                    .sortType(sortType)
                    .searchName(removeSpace(koreanName))
                    .brand(brand)
                    .notePhotos(notePhotos)
                    .expected(true)
                    .build();
        } else if (sortType == 2) {
            return Perfume.builder()
                    .koreanName(koreanName)
                    .englishName(englishName)
                    .volume(volumes)
                    .topNote(topNote)
                    .heartNote(heartNote)
                    .priceVolume(priceVolume)
                    .price(Integer.parseInt(price))
                    .sortType(sortType)
                    .searchName(removeSpace(koreanName))
                    .brand(brand)
                    .notePhotos(notePhotos)
                    .expected(true)
                    .build();
        } else {
            return Perfume.builder()
                    .koreanName(koreanName)
                    .englishName(englishName)
                    .volume(volumes)
                    .topNote(topNote)
                    .heartNote(heartNote)
                    .baseNote(baseNote)
                    .priceVolume(priceVolume)
                    .price(Integer.parseInt(price))
                    .sortType(sortType)
                    .searchName(removeSpace(koreanName))
                    .brand(brand)
                    .notePhotos(notePhotos)
                    .expected(true)
                    .build();
        }
    }

    private String removeSpace(String str){
        String result = str.replaceAll(" ","");
        return result;
    }
}
