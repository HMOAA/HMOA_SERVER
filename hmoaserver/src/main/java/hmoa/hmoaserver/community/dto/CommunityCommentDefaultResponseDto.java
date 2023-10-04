package hmoa.hmoaserver.community.dto;

import hmoa.hmoaserver.common.DateUtils;
import hmoa.hmoaserver.community.domain.CommunityComment;
import hmoa.hmoaserver.member.domain.Member;
import lombok.Data;

@Data
public class CommunityCommentDefaultResponseDto {
    private Long id;
    private String content;
    private String nickname;
    private String profileImg;
    private String createAt;
    private boolean writed;

    public CommunityCommentDefaultResponseDto(CommunityComment comment,boolean writed){
        this.id = comment.getId();
        this.content = comment.getContent();
        this.nickname = comment.getMember().getNickname();
        this.profileImg = comment.getMember().getMemberPhoto().getPhotoUrl();
        this.createAt = DateUtils.calcurateDaysAgo(comment.getCreatedAt());
        this.writed = writed;
    }
}
