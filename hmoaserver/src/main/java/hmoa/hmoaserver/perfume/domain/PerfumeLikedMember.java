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
public class PerfumeLikedMember extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "perfume_liked_member_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "perfume_id")
    private Perfume perfume;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public PerfumeLikedMember(Perfume perfume, Member member) {
        this.perfume = perfume;
        this.member = member;
    }
}
