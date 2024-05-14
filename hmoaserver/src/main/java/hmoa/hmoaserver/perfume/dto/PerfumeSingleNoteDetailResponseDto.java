package hmoa.hmoaserver.perfume.dto;

import hmoa.hmoaserver.perfume.domain.Perfume;
import hmoa.hmoaserver.perfume.review.dto.PerfumeReviewResponseDto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PerfumeSingleNoteDetailResponseDto extends PerfumeDetailsResponseDto{
    private String topNote;

    public PerfumeSingleNoteDetailResponseDto(Perfume perfume, boolean isLiked, PerfumeReviewResponseDto review) {
        super(perfume, isLiked, review);
        this.topNote = perfume.getTopNote();
    }
}
