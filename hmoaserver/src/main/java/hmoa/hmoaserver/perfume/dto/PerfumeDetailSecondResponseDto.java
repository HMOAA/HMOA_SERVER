package hmoa.hmoaserver.perfume.dto;

import hmoa.hmoaserver.perfume.review.dto.PerfumeAgeResponseDto;
import hmoa.hmoaserver.perfume.review.dto.PerfumeGenderResponseDto;
import hmoa.hmoaserver.perfume.review.dto.PerfumeWeatherResponseDto;
import lombok.*;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PerfumeDetailSecondResponseDto {
    private PerfumeCommentGetResponseDto commentInfo;
    private List<PerfumeSimilarResponseDto> similarPerfumes;

    public PerfumeDetailSecondResponseDto(PerfumeCommentGetResponseDto commentInfo, List<PerfumeSimilarResponseDto> similarPerfumes){
        this.commentInfo = commentInfo;
        this.similarPerfumes = similarPerfumes;
    }
}
