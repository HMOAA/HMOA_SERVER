package hmoa.hmoaserver.recommend.survey.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class PerfumeRecommendRequestDto {

    private int minPrice;
    private int maxPrice;
    private List<String> notes;
}
