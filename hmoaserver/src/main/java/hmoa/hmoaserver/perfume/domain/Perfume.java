package hmoa.hmoaserver.perfume.domain;

import hmoa.hmoaserver.admin.domain.HomeMenu;
import hmoa.hmoaserver.brand.domain.Brand;
import hmoa.hmoaserver.common.BaseEntity;
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
public class Perfume extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "perfume_id")
    private Long id;

    @OneToMany(mappedBy = "perfume", cascade = CascadeType.ALL , orphanRemoval = true)
    private List<PerfumePhoto> perfumePhotos = new ArrayList<>();

    private String koreanName;
    private String englishName;
    private int price;
    @ElementCollection
    private List<Integer> volume;
    @ElementCollection
    private List<String> singleNote;
    @ElementCollection
    private List<Integer> notePhotos;
    private int priceVolume;
    private int sortType;
    private String topNote;
    private String heartNote;
    private String baseNote;
    private int heartCount;

    private String searchName;

    @OneToMany(mappedBy = "perfume", cascade = CascadeType.ALL , orphanRemoval = true)
    private List<PerfumeComment> perfumeComments = new ArrayList<>();

    @OneToMany(mappedBy = "perfume", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PerfumeLikedMember> perfumeLikedMembers = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Brand brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "home_menu_id")
    private HomeMenu homeMenu;

    @Builder
    public Perfume(String koreanName, List<Integer> notePhotos, int sortType,List<String> singleNote, String englishName, int price,List<Integer> volume,int priceVolume,String topNote,String heartNote,String baseNote, Brand brand,String searchName) {
        this.koreanName = koreanName;
        this.englishName = englishName;
        this.price = price;
        this.volume=volume;
        this.priceVolume=priceVolume;
        this.topNote=topNote;
        this.heartNote=heartNote;
        this.baseNote=baseNote;
        this.brand = brand;
        this.heartCount = 0;
        this.searchName=searchName;
        this.sortType=sortType;
        this.singleNote=singleNote;
        this.notePhotos = notePhotos;
    }
    public PerfumePhoto getPerfumePhoto() {
        int perfumePhotoSize = this.perfumePhotos.size();
        if (perfumePhotoSize == 0) {
            return null;
        }

        PerfumePhoto perfumePhoto = this.perfumePhotos.get(0);
        return perfumePhoto;
    }

    public PerfumePhoto getPerfumeMainPhoto() {
        int perfumePhotoSize = this.perfumePhotos.size();
        if (perfumePhotoSize == 0) {
            return null;
        }

        PerfumePhoto perfumePhoto = this.perfumePhotos.get(perfumePhotoSize - 1);
        return perfumePhoto;
    }

    public Brand getPerfumeBrand(){
        return this.getBrand();
    }

    public void increaseHeartCount(){
        this.heartCount += 1;
    }

    public void decreaseHeartCount(){
        this.heartCount -= 1;
    }

    public void setHomeMenu(HomeMenu homeMenu){
        this.homeMenu = homeMenu;
    }

    public void deleteHomeMenu(){
        this.homeMenu = null;
    }
}
