package hmoa.hmoaserver.photo.domain;

import hmoa.hmoaserver.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class BasePhoto<T> extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "photo_id")
    private Long id;

    private String photoUrl;

    public abstract T getEntity();

    public BasePhoto(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
