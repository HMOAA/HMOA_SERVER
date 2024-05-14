package hmoa.hmoaserver.perfume.dto;

import hmoa.hmoaserver.perfume.domain.Perfume;
import hmoa.hmoaserver.perfume.review.dto.PerfumeReviewResponseDto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PerfumeThreeNoteDetailResponseDto extends PerfumeDetailsResponseDto{
    private String topNote;
    private String heartNote;
    private String baseNote;

    public PerfumeThreeNoteDetailResponseDto(Perfume perfume, boolean isLiked, PerfumeReviewResponseDto review) {
        super(perfume, isLiked, review);
        this.topNote = perfume.getTopNote();
        this.heartNote = perfume.getHeartNote();
        this.baseNote = perfume.getBaseNote();
    }
}
