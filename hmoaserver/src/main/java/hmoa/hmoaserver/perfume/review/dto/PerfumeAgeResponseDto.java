package hmoa.hmoaserver.perfume.review.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PerfumeAgeResponseDto {
    private int ten;
    private int twenty;
    private int thirty;
    private int fourty;
    private int fifty;
    public PerfumeAgeResponseDto(List<Double> ages){
        if (ages.size()!=0){
            this.ten=(int)Math.round(ages.get(0));
            this.twenty=(int)Math.round(ages.get(1));
            this.thirty=(int)Math.round(ages.get(2));
            this.fourty=(int)Math.round(ages.get(3));
            this.fifty=(int)Math.round(ages.get(4));
        }
    }
}
