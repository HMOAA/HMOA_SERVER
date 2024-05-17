package hmoa.hmoaserver.community.dto;

import hmoa.hmoaserver.community.domain.Community;
import hmoa.hmoaserver.community.domain.CommunityComment;
import hmoa.hmoaserver.member.domain.Member;
import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class CommunityCommentDefaultRequestDto {
    private String content;

    public  CommunityComment toEntity(Member member, Community community) {
        return CommunityComment.builder()
                .community(community)
                .member(member)
                .content(content)
                .build();
    }
}
