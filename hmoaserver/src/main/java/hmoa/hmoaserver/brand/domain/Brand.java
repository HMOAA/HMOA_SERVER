package hmoa.hmoaserver.brand.domain;

import hmoa.hmoaserver.perfume.domain.Perfume;
import hmoa.hmoaserver.photo.domain.BrandPhoto;
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
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "band_id")
    private Long id;

    private String brandName;
    private String englishName;
    private int consonant;
    private int heartCount;

    @OneToMany(mappedBy = "brand", cascade = CascadeType.ALL , orphanRemoval = true)
    private List<BrandPhoto> brandPhotos = new ArrayList<>();

    @OneToMany(mappedBy = "brand", cascade = CascadeType.ALL , orphanRemoval = true)
    private List<Perfume> perfumeList = new ArrayList<>();

    @OneToMany(mappedBy = "brand", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BrandLikedMember> brandLikedMembers = new ArrayList<>();

    @Builder
    public Brand(String brandName, String englishName , int consonant) {
        this.brandName = brandName;
        this.englishName = englishName;
        this.consonant = consonant;
        this.heartCount = 0;
    }

    public BrandPhoto getBrandPhoto() {
        int brandPhotoSize = this.brandPhotos.size();
        if (brandPhotoSize == 0) {
            return null;
        }

        BrandPhoto brandPhoto = this.brandPhotos.get(brandPhotoSize - 1);
        return brandPhoto;
    }

    public void increaseHeartCount(){
        this.heartCount += 1;
    }

    public void decreaseHeartCount(){
        this.heartCount -= 1;
    }

}
