package hmoa.hmoaserver.photo.dto;

import hmoa.hmoaserver.photo.domain.BasePhoto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@ApiModel(value = "Photo 응답")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PhotoResponseDto<T extends BasePhoto> {

    @ApiModelProperty(position = 1, required = true, value = "사진 id", example = "1")
    private Long photoId;

    @ApiModelProperty(position = 2, required = true, value = "사진 URL")
    private String photoUrl;

    public PhotoResponseDto(T entity) {
        this.photoId = entity.getId();
        this.photoUrl = entity.getPhotoUrl();
    }
}
