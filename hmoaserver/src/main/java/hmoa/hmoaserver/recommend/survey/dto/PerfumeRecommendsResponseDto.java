package hmoa.hmoaserver.recommend.survey.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PerfumeRecommendsResponseDto {

    private List<PerfumeRecommendResponseDto> recommendPerfumes;

    public PerfumeRecommendsResponseDto(final List<PerfumeRecommendResponseDto> recommendPerfumes) {
        this.recommendPerfumes = recommendPerfumes;
    }
}
