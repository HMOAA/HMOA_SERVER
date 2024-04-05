package hmoa.hmoaserver.community.dto;

import lombok.Data;

import java.util.List;

@Data
public class CommunityCommentAllResponseDto {
    private long commentCount;
    private boolean lastPage;
    private List<CommunityCommentDefaultResponseDto> comments;

    public CommunityCommentAllResponseDto( long commentCount, boolean lastPage, List<CommunityCommentDefaultResponseDto> comments){
        this.commentCount = commentCount;
        this.lastPage = lastPage;
        this.comments = comments;
    }

}
