package hmoa.hmoaserver.photo.domain;

import hmoa.hmoaserver.magazine.domain.Magazine;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MagazinePhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "magazine_photo_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "magazine_id")
    private Magazine magazine;

    private String photoUrl;

    @Builder
    public MagazinePhoto(Magazine magazine, String photoUrl) {
        this.magazine = magazine;
        this.photoUrl = photoUrl;
    }

}
