package hmoa.hmoaserver.recommend.survey.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL , orphanRemoval = true)
    private List<Question> questions = new ArrayList<>();

    @Builder
    public Survey(String title, SurveyType surveyType) {
        this.title = title;
        this.surveyType = surveyType;
    }
}
