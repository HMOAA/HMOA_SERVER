package hmoa.hmoaserver.recommend.survey.dto;

import hmoa.hmoaserver.community.dto.CommunityByHBTIResponseDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SurveyHomeResponseDto {

    private String backgroundImgUrl;
    private String firstImageUrl;
    private String secondImageUrl;
    @Getter(AccessLevel.NONE)
    private boolean isOrdered;
    private List<CommunityByHBTIResponseDto> reviews;

    public boolean getIsOrdered() {
        return isOrdered;
    }

    public SurveyHomeResponseDto(String backgroundImgUrl, String firstImageUrl, String secondImageUrl, boolean isOrdered, List<CommunityByHBTIResponseDto> reviews) {
        this.backgroundImgUrl = backgroundImgUrl;
        this.firstImageUrl = firstImageUrl;
        this.secondImageUrl = secondImageUrl;
        this.isOrdered = isOrdered;
        this.reviews = reviews;
    }
}
