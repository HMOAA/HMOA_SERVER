package hmoa.hmoaserver.recommend.survey.domain;

import hmoa.hmoaserver.member.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NoteRecommend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "note_recommend_id")
    private Long id;

    @ElementCollection
    private List<String> recommendNotes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public NoteRecommend(List<String> recommendNotes, Member member) {
        this.recommendNotes = recommendNotes;
        this.member = member;
    }
}
