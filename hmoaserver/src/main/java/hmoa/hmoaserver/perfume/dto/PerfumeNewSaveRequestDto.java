package hmoa.hmoaserver.perfume.dto;

import hmoa.hmoaserver.brand.domain.Brand;
import hmoa.hmoaserver.perfume.domain.Perfume;
import lombok.Data;

import java.util.List;

@Data
public class PerfumeNewSaveRequestDto {

    private String koreanName;
    private String englishName;
    private String brandName;
    private String brandKoreanName;
    private String brandEnglishName;
    private String topNote;
    private String heartNote;
    private String baseNote;
    private String singleNote;
    private String notePhotos;
    private String volumeAndPrice;
    private int imgIndex;

    public Perfume toEntity(Brand brand, int sortType, int price, List<Integer> volumes, List<Integer> notePhotos, int priceVolume) {

        if (sortType == 0) {
            return Perfume.builder()
                    .koreanName(koreanName)
                    .englishName(englishName)
                    .volume(volumes)
                    .priceVolume(priceVolume)
                    .price(price)
                    .sortType(sortType)
                    .searchName(removeSpace(koreanName))
                    .brand(brand)
                    .notePhotos(notePhotos)
                    .imgIndex(imgIndex)
                    .build();
        } else if (sortType == 1) {
            return Perfume.builder()
                    .koreanName(koreanName)
                    .englishName(englishName)
                    .volume(volumes)
                    .topNote(topNote)
                    .priceVolume(priceVolume)
                    .price(price)
                    .sortType(sortType)
                    .searchName(removeSpace(koreanName))
                    .brand(brand)
                    .notePhotos(notePhotos)
                    .imgIndex(imgIndex)
                    .build();
        } else if (sortType == 2) {
            return Perfume.builder()
                    .koreanName(koreanName)
                    .englishName(englishName)
                    .volume(volumes)
                    .topNote(topNote)
                    .heartNote(heartNote)
                    .priceVolume(priceVolume)
                    .price(price)
                    .sortType(sortType)
                    .searchName(removeSpace(koreanName))
                    .brand(brand)
                    .notePhotos(notePhotos)
                    .imgIndex(imgIndex)
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
                    .price(price)
                    .sortType(sortType)
                    .searchName(removeSpace(koreanName))
                    .brand(brand)
                    .notePhotos(notePhotos)
                    .imgIndex(imgIndex)
                    .build();
        }
    }

    private String removeSpace(String str) {
        String result = str.replaceAll(" ", "");
        return result;
    }
}
