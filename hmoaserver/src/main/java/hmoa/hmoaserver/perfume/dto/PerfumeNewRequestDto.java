package hmoa.hmoaserver.perfume.dto;

import lombok.Data;

import java.util.List;

@Data
public class PerfumeNewRequestDto {
    private String brandName;
    private String KoreanName;
    private String englishName;
    private int price;
    private String volumes;
    private int priceVloume;
    private int sortType;
    private String topNote;
    private String heartNote;
    private String baseNote;
    private String notePhotos;
}
