package hmoa.hmoaserver.community.dto;

import hmoa.hmoaserver.community.domain.Category;
import hmoa.hmoaserver.community.domain.Community;
import lombok.Data;

@Data
public class CommunityByCategoryResponseDto {
    private Long communityId;
    private Category category;
    private String title;
    private int commentCount;
    private int heartCount;
    private boolean isLiked = false;

    public CommunityByCategoryResponseDto(Community community){
        this.communityId = community.getId();
        this.category = community.getCategory();
        this.title = community.getTitle();
        this.commentCount = community.getCommunityComments().size();
        this.heartCount = community.getHeartCount();
    }

    public CommunityByCategoryResponseDto(Community community, boolean isLiked){
        this.communityId = community.getId();
        this.category = community.getCategory();
        this.title = community.getTitle();
        this.commentCount = community.getCommunityComments().size();
        this.heartCount = community.getHeartCount();
        this.isLiked = isLiked;
    }
}
