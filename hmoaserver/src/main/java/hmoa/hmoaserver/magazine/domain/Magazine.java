package hmoa.hmoaserver.magazine.domain;

import hmoa.hmoaserver.common.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Magazine extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "magazine_id")
    private long id;
    private int viewCount;
    private int likeCount;
    @ElementCollection
    private List<String> tags;
    @OneToMany(mappedBy = "magazine", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ContentRequest> contents = new ArrayList<>();

    @Builder
    public Magazine(List<ContentRequest> contents, List<String> tags) {
        this.contents = contents;
        this.tags = tags;
        this.viewCount = 0;
        this.likeCount = 0;
    }

    public void setContents(List<ContentRequest> contents) {
        this.contents = contents;
    }
}
