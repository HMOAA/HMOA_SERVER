package hmoa.hmoaserver.photo.domain;

import hmoa.hmoaserver.common.BaseEntity;
import hmoa.hmoaserver.community.domain.Community;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommunityPhoto extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "community_photo_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community_id")
    private Community community;

    private String photoUrl;

    @Builder
    public CommunityPhoto(Community community, String photoUrl) {
        this.community = community;
        this.photoUrl = photoUrl;
    }
}
