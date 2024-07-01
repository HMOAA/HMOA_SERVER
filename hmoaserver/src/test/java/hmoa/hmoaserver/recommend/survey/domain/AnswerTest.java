package hmoa.hmoaserver.recommend.survey.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AnswerTest {

    @Test
    @DisplayName("답변 생성 테스트")
    void 답변이_생성된다() {
        //given
        Survey survey = Survey.builder().surveyType(SurveyType.NOTE).title("향료 추천").build();
        Question question = Question.builder().content("좋아하는 계절이 있으신가요?").survey(survey).build();
        Answer answer = Answer.builder().content("겨울").question(question).build();

        //when, then
        Assertions.assertThat(answer.getContent()).isEqualTo("겨울");
        Assertions.assertThat(answer.getQuestion()).isEqualTo(question);
        Assertions.assertThat(answer.getQuestion().getSurvey()).isEqualTo(survey);
    }
}