package hmoa.hmoaserver.perfume.review.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PerfumeAgeResponseDto {
    private boolean isWrited;
    private int age;
    public PerfumeAgeResponseDto(Double age,boolean isWrited){
        this.isWrited=isWrited;
        if (age==0.0){
            this.age=0;
        }else this.age=(int) Math.round(age);
    }
}
