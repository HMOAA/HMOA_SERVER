package hmoa.hmoaserver.recommend.survey.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuestionTest {

    @Test
    @DisplayName("질문 생성 테스트")
    void 질문이_생성된다() {
        //given
        Survey survey = Survey.builder().surveyType(SurveyType.NOTE).title("향료 추천").build();
        Question question = Question.builder().content("좋아하는 계절이 있으신가요?").survey(survey).build();

        //when, then
        Assertions.assertThat(question.getContent()).isEqualTo("좋아하는 계절이 있으신가요?");
        Assertions.assertThat(question.getSurvey()).isEqualTo(survey);
    }

}