package hmoa.hmoaserver.perfume.dto;

import hmoa.hmoaserver.perfume.review.dto.PerfumeAgeResponseDto;
import hmoa.hmoaserver.perfume.review.dto.PerfumeGenderResponseDto;
import hmoa.hmoaserver.perfume.review.dto.PerfumeWeatherResponseDto;
import lombok.*;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PerfumeGetSecondResponseDto {
    private PerfumeWeatherResponseDto weather;
    private PerfumeGenderResponseDto gender;
    private PerfumeAgeResponseDto age;

    private PerfumeCommentGetResponseDto commentInfo;


    public PerfumeGetSecondResponseDto(PerfumeAgeResponseDto age, PerfumeWeatherResponseDto weather, PerfumeGenderResponseDto gender){
        this.weather=weather;
        this.gender=gender;
        this.age=age;
    }
}
