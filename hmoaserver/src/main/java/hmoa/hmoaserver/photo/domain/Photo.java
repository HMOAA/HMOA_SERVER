package hmoa.hmoaserver.photo.domain;

import hmoa.hmoaserver.common.BaseEntity;
import hmoa.hmoaserver.perfume.domain.Perfume;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter    
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Photo extends BaseEntity {

    @Id
    @Column(name = "photo_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String folderName;
    private String fileName;
    private String photoUrl;

    @ManyToOne
    @JoinColumn(name = "perfume_id")
    private Perfume perfume;

    @Builder
    public Photo(Long id, String folderName, String fileName, String photoUrl, Perfume perfume) {
        this.id = id;
        this.folderName = folderName;
        this.fileName = fileName;
        this.photoUrl = photoUrl;
        this.perfume = perfume;
    }

}
