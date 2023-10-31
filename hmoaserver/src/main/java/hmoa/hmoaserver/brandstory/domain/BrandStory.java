package hmoa.hmoaserver.brandstory.domain;

import hmoa.hmoaserver.common.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Table(name = "BrandStory")
@AllArgsConstructor
public class BrandStory extends BaseEntity {
    @Id
    @Column(name = "brandStory_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;
    private String subtitle;

    private String content;
    private boolean isDeleted = false;

    public void updateContent(String content) {
        this.content = content;
    }

    public void delete() {
        isDeleted = true;
    }
}
