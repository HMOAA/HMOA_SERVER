package hmoa.hmoaserver.recommend.survey.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Long id;

    private float point;

    private String content;

    @Enumerated(EnumType.STRING)
    private QuestionType questionType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id")
    private Survey survey;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL , orphanRemoval = true)
    private List<Answer> answers = new ArrayList<>();

    @Builder
    public Question(String content, QuestionType questionType, Survey survey) {
        this.content = content;
        this.questionType = questionType;
        this.survey = survey;
    }

    public boolean isMultiple() {
        return questionType == QuestionType.MULTIPLE;
    }
}
