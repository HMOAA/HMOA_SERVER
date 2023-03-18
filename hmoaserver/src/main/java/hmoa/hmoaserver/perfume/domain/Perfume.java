package hmoa.hmoaserver.perfume.domain;

import hmoa.hmoaserver.brand.domain.Brand;
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
public class Perfume {
    @Id
    @Column(name = "perfume_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "photo", cascade = CascadeType.ALL , orphanRemoval = true)
    private List<Photo> photo = new ArrayList<>();

    private String description;
    private String scent;
    private Long price;

    @OneToMany(mappedBy = "perfumeComment", cascade = CascadeType.ALL , orphanRemoval = true)
    private List<PerfumeComment> perfumeComments = new ArrayList<>();

    @ManyToOne
    @JoinColumn
    private Brand brand;

    @Builder
    public Perfume(Long id, String name, List<Photo> photo, String description, String scent, Long price, List<PerfumeComment> perfumeComments, Brand brand) {
        this.id = id;
        this.name = name;
        this.photo = photo;
        this.description = description;
        this.scent = scent;
        this.price = price;
        this.perfumeComments = perfumeComments;
        this.brand = brand;
    }
}
