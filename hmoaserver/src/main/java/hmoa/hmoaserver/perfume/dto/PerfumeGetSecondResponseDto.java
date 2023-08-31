package hmoa.hmoaserver.perfume.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PerfumeGetSecondResponseDto {
    private int spring;
    private int summer;
    private int autumn;
    private int winter;

    public PerfumeGetSecondResponseDto(List<Double> weatherPercentage){
        if (weatherPercentage.size()!=0) {
            this.spring = (int) Math.round(weatherPercentage.get(0));
            this.summer = (int) Math.round(weatherPercentage.get(1));
            this.autumn = (int) Math.round(weatherPercentage.get(2));
            this.winter = (int) Math.round(weatherPercentage.get(3));
        }
    }
}
