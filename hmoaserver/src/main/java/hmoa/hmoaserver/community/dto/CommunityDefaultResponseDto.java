package hmoa.hmoaserver.community.dto;

import hmoa.hmoaserver.common.DateUtils;
import hmoa.hmoaserver.common.DefaultValues;
import hmoa.hmoaserver.community.domain.Category;
import hmoa.hmoaserver.community.domain.Community;
import hmoa.hmoaserver.photo.domain.CommunityPhoto;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CommunityDefaultResponseDto {
    private Long id;
    private String title;
    private Category category;
    private String content;
    private String author;
    private String profileImgUrl;
    private int imagesCount;
    private List<CommunityPhotoDefaultResponseDto> communityPhotos = new ArrayList<>();
    private String myProfileImgUrl = DefaultValues.getDEFALUT_PROFILE_URL();
    private String time;
    private boolean writed = false;

    public CommunityDefaultResponseDto(Community community){
        this.id=community.getId();
        this.title=community.getTitle();
        this.category=community.getCategory();
        this.content=community.getContent();
        this.author=community.getMember().getNickname();
        this.profileImgUrl=community.getMember().getMemberPhoto().getPhotoUrl();
        this.time= DateUtils.calcurateDaysAgo(community.getCreatedAt());

        if (community.getCommunityPhotos() != null) {
            List<CommunityPhoto> communityPhotos = community.getCommunityPhotos();
            this.imagesCount = communityPhotos.size();

            for (CommunityPhoto communityPhoto : communityPhotos) {
                this.communityPhotos.add(new CommunityPhotoDefaultResponseDto(communityPhoto));
            }
        }
    }
    public CommunityDefaultResponseDto(Community community, boolean writed){
        this.id=community.getId();
        this.title=community.getTitle();
        this.category=community.getCategory();
        this.content=community.getContent();
        this.author=community.getMember().getNickname();
        this.profileImgUrl=community.getMember().getMemberPhoto().getPhotoUrl();
        this.time= DateUtils.calcurateDaysAgo(community.getCreatedAt());
        this.writed = writed;

        if (community.getCommunityPhotos() != null) {
            List<CommunityPhoto> communityPhotos = community.getCommunityPhotos();
            this.imagesCount = communityPhotos.size();

            for (CommunityPhoto communityPhoto : communityPhotos) {
                this.communityPhotos.add(new CommunityPhotoDefaultResponseDto(communityPhoto));
            }
        }
    }
}
