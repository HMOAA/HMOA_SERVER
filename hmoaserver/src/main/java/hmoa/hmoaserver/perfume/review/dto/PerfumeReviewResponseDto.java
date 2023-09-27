package hmoa.hmoaserver.perfume.review.dto;

import lombok.Data;

@Data
public class PerfumeReviewResponseDto {
    private PerfumeWeatherResponseDto weather;
    private PerfumeGenderResponseDto gender;
    private PerfumeAgeResponseDto age;

    public PerfumeReviewResponseDto(PerfumeAgeResponseDto age, PerfumeWeatherResponseDto weather, PerfumeGenderResponseDto gender){
        this.weather = weather;
        this.age = age;
        this.gender = gender;
    }
}
