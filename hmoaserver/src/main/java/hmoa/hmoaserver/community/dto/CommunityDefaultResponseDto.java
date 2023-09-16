package hmoa.hmoaserver.community.dto;

import hmoa.hmoaserver.common.DateUtils;
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

    private String time;
    private boolean writed;

    public CommunityDefaultResponseDto(Community community,boolean writed){
        this.id=community.getId();
        this.title=community.getTitle();
        this.category=community.getCategory();
        this.content=community.getContent();
        this.author=community.getMember().getNickname();
        this.profileImgUrl=community.getMember().getMemberPhoto().getPhotoUrl();
        this.time= DateUtils.calcurateDaysAgo(community.getCreatedAt());
        this.writed = writed;
    }
}
