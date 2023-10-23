package hmoa.hmoaserver.community.dto;

import hmoa.hmoaserver.photo.domain.CommunityPhoto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@ApiModel(value = "CommunityPhoto 기본 응답")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommunityPhotoDefaultResponseDto {

    @ApiModelProperty(position = 1, required = true, value = "게시글 사진 식별자", example = "1")
    private Long photoId;

    @ApiModelProperty(position = 2, required = true, value = "사진 URL")
    private String photoUrl;

    public CommunityPhotoDefaultResponseDto(CommunityPhoto communityPhoto) {
        this.photoId = communityPhoto.getId();
        this.photoUrl = communityPhoto.getPhotoUrl();
    }
}
