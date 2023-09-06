package hmoa.hmoaserver.perfume.review.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PerfumeAgeResponseDto {
    private int age;
    public PerfumeAgeResponseDto(Double age){
        if (age==0.0){
            this.age=(int)Math.round(age);
        }
    }
}
