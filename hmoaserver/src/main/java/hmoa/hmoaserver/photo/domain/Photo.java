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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String folderName;
    private String fileName;
    private String photoUrl;

    @Builder
    public Photo(Long id, String folderName, String fileName, String photoUrl) {
        this.id = id;
        this.folderName = folderName;
        this.fileName = fileName;
        this.photoUrl = photoUrl;
    }

}
