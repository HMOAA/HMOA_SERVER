package hmoa.hmoaserver.community.dto;

import hmoa.hmoaserver.community.domain.Category;
import hmoa.hmoaserver.community.domain.Community;
import lombok.Data;

@Data
public class CommunityByCategoryResponseDto {
    private Long communityId;
    private Category category;
    private String title;
    public CommunityByCategoryResponseDto(Community community){
        this.communityId = community.getId();
        this.category = community.getCategory();
        this.title = community.getTitle();
    }
}
