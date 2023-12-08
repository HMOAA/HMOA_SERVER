package hmoa.hmoaserver.community.dto;

import hmoa.hmoaserver.common.DateUtils;
import hmoa.hmoaserver.community.domain.CommunityComment;
import hmoa.hmoaserver.member.domain.Member;
import lombok.Data;

@Data
public class CommunityCommentDefaultResponseDto {
    private Long commentId;
    private String content;
    private String author;
    private String profileImg;
    private String time;
    private boolean writed;

    public CommunityCommentDefaultResponseDto(CommunityComment comment, boolean writed){
        this.commentId = comment.getId();
        this.content = comment.getContent();
        this.author = comment.getMember().getNickname();
        this.profileImg = comment.getMember().getMemberPhoto().getPhotoUrl();
        this.time = DateUtils.calcurateDaysAgo(comment.getCreatedAt());
        this.writed = writed;
    }
}
