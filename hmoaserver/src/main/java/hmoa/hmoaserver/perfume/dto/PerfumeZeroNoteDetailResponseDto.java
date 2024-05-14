package hmoa.hmoaserver.perfume.dto;


import hmoa.hmoaserver.perfume.domain.Perfume;
import hmoa.hmoaserver.perfume.review.dto.PerfumeReviewResponseDto;

public class PerfumeZeroNoteDetailResponseDto extends PerfumeDetailsResponseDto{
    public PerfumeZeroNoteDetailResponseDto(Perfume perfume, boolean isLiked, PerfumeReviewResponseDto review) {
        super(perfume, isLiked, review);
    }
}
