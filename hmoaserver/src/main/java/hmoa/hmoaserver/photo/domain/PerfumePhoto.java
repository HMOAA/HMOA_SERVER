package hmoa.hmoaserver.photo.domain;

import hmoa.hmoaserver.common.BaseEntity;
import hmoa.hmoaserver.perfume.domain.Perfume;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PerfumePhoto extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "perfume_photo_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "perfume_id")
    private Perfume perfume;

    private String photoUrl;

    @Builder
    public PerfumePhoto(Perfume perfume, String photoUrl) {
        this.perfume = perfume;
        this.photoUrl = photoUrl;
    }
}
