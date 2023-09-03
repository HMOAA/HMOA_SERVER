package hmoa.hmoaserver.perfume.review.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PerfumeGenderResponseDto {
    private int man;
    private int woman;
    private int neuteur;
    public PerfumeGenderResponseDto(List<Double> genders){
        if (genders.size()!=0){
            this.man=(int)Math.round(genders.get(0));
            this.woman=(int)Math.round(genders.get(1));
            this.neuteur=(int)Math.round(genders.get(2));
        }
    }
}
