package hmoa.hmoaserver.recommend.survey.dto;

import hmoa.hmoaserver.recommend.survey.domain.Survey;
import hmoa.hmoaserver.recommend.survey.domain.SurveyType;
import lombok.Data;

@Data
public class SurveySaveRequestDto {
    private String title;
    private SurveyType surveyType;

    public Survey toEntity() {
        return Survey.builder()
                .title(title)
                .surveyType(surveyType)
                .build();
    }
}
