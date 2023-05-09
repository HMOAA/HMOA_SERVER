package hmoa.hmoaserver.perfume.domain;

import hmoa.hmoaserver.common.BaseEntity;
import hmoa.hmoaserver.member.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PerfumeComment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String comment;

    private int heart;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "perfume_id")
    private Perfume perfume;

    @Builder
    public PerfumeComment(Long id, String comment, int heart, Member member, Perfume perfume) {
        this.id = id;
        this.comment = comment;
        this.heart = 0;
        this.member = member;
        this.perfume = perfume;
    }

    public void increaseHeartCount(){
        this.heart+=1;
    }

    public void decreaseHeartCount(){
        this.heart-=1;
    }
}
