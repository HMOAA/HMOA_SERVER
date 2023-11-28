package hmoa.hmoaserver.perfume.review.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PerfumeGenderResponseDto {
    private boolean isWrited;
    private int selected;
    private int man;
    private int woman;
    private int neuter;

    public PerfumeGenderResponseDto(List<Double> genders, boolean isWrited, int selected){
        this.isWrited=isWrited;
        this.selected = selected;
        if (genders.size()!=0){
            this.man=(int)Math.round(genders.get(0));
            this.woman=(int)Math.round(genders.get(1));
            this.neuter = (int) Math.round(genders.get(2));
        }
    }
}
