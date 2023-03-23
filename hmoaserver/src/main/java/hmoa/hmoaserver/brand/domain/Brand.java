package hmoa.hmoaserver.brand.domain;

import hmoa.hmoaserver.perfume.domain.Perfume;
import hmoa.hmoaserver.photo.domain.Photo;
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
    private Long id;

    private String name;

    @OneToMany(mappedBy = "brand", cascade = CascadeType.ALL , orphanRemoval = true)
    private List<Perfume> perfumeList = new ArrayList<>();


    /*@OneToOne(mappedBy = "brand")
    private Photo photo;

    @Builder
    public Brand(Long id, String name, Photo photo, List<Perfume> perfumeList) {
        this.id = id;
        this.name = name;
        this.photo = photo;
        this.perfumeList = perfumeList;
    }*/
}
