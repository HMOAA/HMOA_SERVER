package hmoa.hmoaserver.recommend.survey.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id")
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    @OneToMany(mappedBy = "answer", cascade = CascadeType.ALL , orphanRemoval = true)
    private List<AnswerNote> answerNotes = new ArrayList<>();

    @OneToMany(mappedBy = "answer", cascade = CascadeType.ALL , orphanRemoval = true)
    private List<MemberAnswer> memberAnswers = new ArrayList<>();

    @Builder
    public Answer(String content, Question question) {
        this.content = content;
        this.question = question;
    }
}
