package hmoa.hmoaserver.perfumer.domain;

import hmoa.hmoaserver.common.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Table(name = "Perfumer")
@AllArgsConstructor
public class Perfumer extends BaseEntity {
    @Id
    @Column(name = "perfumer_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;
    private String subTitle;

    private String content;
    private boolean isDeleted = false;

    public void updateContent(String content) {
        this.content = content;
    }

    public void delete() {
        isDeleted = true;
    }
}
