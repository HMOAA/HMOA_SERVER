package hmoa.hmoaserver.perfume.dto;

import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.perfume.domain.Perfume;
import hmoa.hmoaserver.perfume.domain.PerfumeComment;
import lombok.Data;

@Data
public class PerfumeCommentRequestDto {
    private String content;
    public PerfumeComment toEntity(Member member, Perfume perfume){
        return PerfumeComment.builder()
                .content(content)
                .heartCount(0)
                .member(member)
                .perfume(perfume)
                .build();
    }
}
