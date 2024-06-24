package hmoa.hmoaserver.recommend.survey.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Survey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "survey_id")
    private Long id;

    private String title;

    @Enumerated(EnumType.STRING)
    private SurveyType surveyType;

    @Builder
    public Survey(String title, SurveyType surveyType) {
        this.title = title;
        this.surveyType = surveyType;
    }
}
