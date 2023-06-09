package hmoa.hmoaserver.photo.domain;

import hmoa.hmoaserver.common.BaseEntity;
import hmoa.hmoaserver.member.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberPhoto extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_photo_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String photoUrl;

    private boolean isDeleted;

    @Builder
    public MemberPhoto(Member member, String photoUrl) {
        this.member = member;
        this.photoUrl = photoUrl;
        this.isDeleted = false;
    }

    public void removePhotoUrl() {
        this.photoUrl = null;
    }

    public void delete() {
        this.isDeleted = true;
    }
}
