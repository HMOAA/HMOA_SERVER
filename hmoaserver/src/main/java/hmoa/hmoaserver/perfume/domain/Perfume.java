package hmoa.hmoaserver.perfume.domain;

import hmoa.hmoaserver.brand.domain.Brand;
import hmoa.hmoaserver.photo.domain.PerfumePhoto;
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
public class Perfume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "perfume_id")
    private Long id;

    @OneToMany(mappedBy = "perfume", cascade = CascadeType.ALL , orphanRemoval = true)
    private List<PerfumePhoto> perfumePhotos = new ArrayList<>();

    private String koreanName;
    private String englishName;
    private String perfumeInfo;
    private Long price;

    @OneToMany(mappedBy = "perfume", cascade = CascadeType.ALL , orphanRemoval = true)
    private List<PerfumeComment> perfumeComments = new ArrayList<>();

    @ManyToOne
    @JoinColumn
    private Brand brand;

    @Builder
    public Perfume(String koreanName, String englishName, String perfumeInfo, Long price, Brand brand) {
        this.koreanName = koreanName;
        this.englishName = englishName;
        this.perfumeInfo = perfumeInfo;
        this.price = price;
        this.brand = brand;
    }

    public PerfumePhoto getPerfumePhoto() {
        int perfumePhotoSize = this.perfumePhotos.size();
        if (perfumePhotoSize == 0) {
            return null;
        }

        PerfumePhoto perfumePhoto = this.perfumePhotos.get(perfumePhotoSize - 1);
        return perfumePhoto;
    }
}
