package hmoa.hmoaserver.recommend.survey.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SurveyHomeResponseDto {

    private String backgroundImgUrl;
    private String firstImageUrl;
    private String secondImageUrl;
    @Getter(AccessLevel.NONE)
    private boolean isOrdered;

    public boolean getIsOrdered() {
        return isOrdered;
    }

    public SurveyHomeResponseDto(String backgroundImgUrl, String firstImageUrl, String secondImageUrl, boolean isOrdered) {
        this.backgroundImgUrl = backgroundImgUrl;
        this.firstImageUrl = firstImageUrl;
        this.secondImageUrl = secondImageUrl;
        this.isOrdered = isOrdered;
    }
}
