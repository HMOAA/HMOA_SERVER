package hmoa.hmoaserver.community.dto;

import hmoa.hmoaserver.common.DateUtils;
import hmoa.hmoaserver.community.domain.Category;
import hmoa.hmoaserver.community.domain.Community;
import hmoa.hmoaserver.photo.domain.CommunityPhoto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommunityByHBTIResponseDto {

    private Long communityId;
    private int imagesCount;
    private Category category;
    private String profileImgUrl;
    private List<CommunityPhotoDefaultResponseDto> communityPhotos = new ArrayList<>();
    private String nickname;
    private String createdAt;
    private int heartCount;
    private String content;

    public CommunityByHBTIResponseDto(Community community) {
        this.communityId = community.getId();
        this.category = community.getCategory();
        this.nickname = community.getMember().getNickname();
        this.createdAt = DateUtils.calculateDaysAgo(community.getCreatedAt());
        this.profileImgUrl = community.getMember().getMemberPhoto().getPhotoUrl();
        this.heartCount = community.getHeartCount();
        this.content = community.getContent();

        if(community.getCommunityPhotos() != null) {
            List<CommunityPhoto> communityPhotos = community.getCommunityPhotos();
            this.imagesCount = communityPhotos.size();

            for (CommunityPhoto communityPhoto : communityPhotos) {
                this.communityPhotos.add(new CommunityPhotoDefaultResponseDto(communityPhoto));
            }
        }
    }
}
