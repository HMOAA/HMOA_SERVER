package hmoa.hmoaserver.perfume.dto;

import hmoa.hmoaserver.perfume.domain.Perfume;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PerfumeDefaultResponseDto {

    private Long perfumeId;
    private String brandName;
    private String koreanName;
    private String englishName;
    private int price;
    private List<Integer> volume;
    private int priceVolume;
    private String topNote;
    private String heartNote;
    private String baseNote;

    public PerfumeDefaultResponseDto(Perfume perfume) {
        this.perfumeId = perfume.getId();
        this.brandName = perfume.getBrand().getBrandName();
        this.koreanName = perfume.getKoreanName();
        this.englishName = perfume.getEnglishName();
        this.volume=perfume.getVolume();
        this.topNote=perfume.getTopNote();
        this.heartNote=perfume.getHeartNote();
        this.baseNote=perfume.getBaseNote();
        this.priceVolume=perfume.getPriceVolume();
        this.price = perfume.getPrice();
    }
}
