package hmoa.hmoaserver.community.dto;

import hmoa.hmoaserver.common.DateUtils;
import hmoa.hmoaserver.community.domain.CommunityComment;
import hmoa.hmoaserver.member.domain.Member;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommunityCommentByMemberResponseDto {
    private Long parentId;
    private Long id;
    private String content;
    private int heartCount;
    private String nickname;
    private String profileImg;
    private String createAt;
    private boolean isLiked = false;
    private boolean isWrited = false;

    public CommunityCommentByMemberResponseDto(CommunityComment comment, boolean isLiked, boolean isWrited){
        if (comment.getCommunity() == null) {
            this.parentId = -1L;
        }
        else {
            this.parentId = comment.getCommunity().getId();
        }
        this.id = comment.getId();
        this.content = comment.getContent();
        this.heartCount = comment.getHeartCount();
        this.nickname = comment.getMember().getNickname();
        this.profileImg = comment.getMember().getMemberPhoto().getPhotoUrl();
        this.createAt = DateUtils.calcurateDaysAgo(comment.getCreatedAt());
        this.isLiked = isLiked;
        this.isWrited = isWrited;
    }
}
