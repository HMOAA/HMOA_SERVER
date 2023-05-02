package hmoa.hmoaserver.perfume.domain;

import hmoa.hmoaserver.common.BaseEntity;
import hmoa.hmoaserver.member.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PerfumeComment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String comment;
    private Boolean heart;

    @ManyToOne
    @JoinColumn(name = "member")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "perfume")
    private Perfume perfume;

    @Builder
    public PerfumeComment(Long id, String comment, Boolean heart, Member member, Perfume perfume) {
        this.id = id;
        this.comment = comment;
        this.heart = heart;
        this.member = member;
        this.perfume = perfume;
    }
}
