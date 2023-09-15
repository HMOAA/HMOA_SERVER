package hmoa.hmoaserver.community.dto;

import hmoa.hmoaserver.community.domain.Category;
import hmoa.hmoaserver.community.domain.Community;
import lombok.Data;

@Data
public class CommunityDefaultResponseDto {
    private Long id;
    private String title;
    private Category category;
    private String content;
    private String author;
    private String profileImgUrl;

    public CommunityDefaultResponseDto(Community community){
        this.id=community.getId();
        this.title=community.getTitle();
        this.category=community.getCategory();
        this.content=community.getContent();
        this.author=community.getMember().getNickname();
        this.profileImgUrl=community.getMember().getMemberPhoto().getPhotoUrl();
    }
}
