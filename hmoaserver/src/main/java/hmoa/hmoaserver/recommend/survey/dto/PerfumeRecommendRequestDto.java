package hmoa.hmoaserver.recommend.survey.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class PerfumeRecommendRequestDto {

    private int price;
    private List<String> notes;
}
