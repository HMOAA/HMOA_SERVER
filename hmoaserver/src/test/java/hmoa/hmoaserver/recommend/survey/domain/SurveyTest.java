package hmoa.hmoaserver.recommend.survey.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SurveyTest {

    @Test
    @DisplayName("설문 생성 테스트")
    void 설문이_생성된다() {
        //given
        Survey survey1 = Survey.builder().title("향료 추천").surveyType(SurveyType.NOTE).build();
        Survey survey2 = Survey.builder().title("향수 추천").surveyType(SurveyType.PERFUME).build();

        //when, then
        Assertions.assertThat(survey1.getSurveyType()).isEqualTo(SurveyType.NOTE);
        Assertions.assertThat(survey1.getTitle()).isEqualTo("향료 추천");

        Assertions.assertThat(survey2.getSurveyType()).isEqualTo(SurveyType.PERFUME);
        Assertions.assertThat(survey2.getTitle()).isEqualTo("향수 추천");
    }
}