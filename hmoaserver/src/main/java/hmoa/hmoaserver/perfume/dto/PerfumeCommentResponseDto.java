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
    private String content;
    private int likeCount;
    private String nickname;
    private Long perfumeId;

    public PerfumeCommentResponseDto(PerfumeComment perfumeComment){
        this.id=perfumeComment.getId();
        this.content=perfumeComment.getContent();
        this.likeCount=perfumeComment.getHeartCount();
        this.nickname=perfumeComment.getMember().getNickname();
        this.perfumeId=perfumeComment.getPerfume().getId();
    }

}
