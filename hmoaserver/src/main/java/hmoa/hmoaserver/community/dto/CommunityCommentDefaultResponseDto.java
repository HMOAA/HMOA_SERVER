package hmoa.hmoaserver.community.dto;

import hmoa.hmoaserver.common.DateUtils;
import hmoa.hmoaserver.community.domain.CommunityComment;

import lombok.Data;

@Data
public class CommunityCommentDefaultResponseDto {
    private Long commentId;
    private String content;
    private String author;
    private String profileImg;
    private String time;
    private boolean writed = false;
    private boolean liked = false;
    private int heartCount;

    public CommunityCommentDefaultResponseDto(CommunityComment comment){
        this.commentId = comment.getId();
        this.content = comment.getContent();
        this.author = comment.getMember().getNickname();
        this.profileImg = comment.getMember().getMemberPhoto().getPhotoUrl();
        this.time = DateUtils.calculateDaysAgo(comment.getCreatedAt());
        this.heartCount = comment.getHeartCount();
    }

    public CommunityCommentDefaultResponseDto(CommunityComment comment, boolean writed, boolean liked){
        this.commentId = comment.getId();
        this.content = comment.getContent();
        this.author = comment.getMember().getNickname();
        this.profileImg = comment.getMember().getMemberPhoto().getPhotoUrl();
        this.time = DateUtils.calculateDaysAgo(comment.getCreatedAt());
        this.liked = liked;
        this.writed = writed;
        this.heartCount = comment.getHeartCount();
    }
}
