package hmoa.hmoaserver.term.domain;

import hmoa.hmoaserver.common.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Table(name = "Term")
@AllArgsConstructor
public class Term extends BaseEntity {
    @Id
    @Column(name = "term_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String Title;
    private String englishTitle;

    private String content;
    private boolean isDeleted = false;

    public void updateTermContent(String content) {
        this.content = content;
    }

    public void deleteTerm() {
        isDeleted = true;
    }

}
