package hmoa.hmoaserver.photo.domain;

import hmoa.hmoaserver.brand.domain.Brand;
import hmoa.hmoaserver.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BrandPhoto extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brand_photo_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private Brand brand;

    private String photoUrl;

    @Builder
    public BrandPhoto(Brand brand, String photoUrl) {
        this.brand = brand;
        this.photoUrl = photoUrl;
    }
}
