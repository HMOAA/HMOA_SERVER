package hmoa.hmoaserver.community.dto;

import lombok.Data;

import java.util.List;

@Data
public class CommunityCommentAllResponseDto {
    private long commentCount;
    private List<CommunityCommentDefaultResponseDto> comments;

    public CommunityCommentAllResponseDto( long commentCount, List<CommunityCommentDefaultResponseDto> comments){
        this.commentCount = commentCount;
        this.comments = comments;
    }

}
