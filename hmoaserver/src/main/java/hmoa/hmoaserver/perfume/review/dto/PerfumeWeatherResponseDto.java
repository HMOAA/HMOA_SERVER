package hmoa.hmoaserver.perfume.review.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PerfumeWeatherResponseDto {
    private int spring;
    private int summer;
    private int autumn;
    private int winter;

    public PerfumeWeatherResponseDto(List<Double> weathers){
        if (weathers.size()!=0) {
            this.spring = (int) Math.round(weathers.get(0));
            this.summer = (int) Math.round(weathers.get(1));
            this.autumn = (int) Math.round(weathers.get(2));
            this.winter = (int) Math.round(weathers.get(3));
        }
    }
}
