package hmoa.hmoaserver.perfume.dto;

import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.perfume.domain.Perfume;
import hmoa.hmoaserver.perfume.domain.PerfumeComment;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PerfumeCommentResponseDto {
    private Long id;
    private String comment;
    private boolean heart;
    private Member member;
    private Perfume perfume;

    public PerfumeCommentResponseDto(PerfumeComment perfumeComment){
        this.id=perfumeComment.getId();
        this.comment=perfumeComment.getComment();
        this.heart=perfumeComment.getHeart();
        this.member=perfumeComment.getMember();
        this.perfume=perfumeComment.getPerfume();
    }

}
