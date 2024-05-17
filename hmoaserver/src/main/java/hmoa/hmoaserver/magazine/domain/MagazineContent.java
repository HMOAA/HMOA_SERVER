package hmoa.hmoaserver.magazine.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MagazineContent {
    @Id
    @Column(name = "magazine_content_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String type;
    @Lob
    private String data;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "magazine_id", nullable = false)
    private Magazine magazine;

    @Builder
    public MagazineContent(String type, String data, Magazine magazine) {
        this.type = type;
        this.data = data;
        this.magazine = magazine;
    }

    public void setData(String data) {
        this.data = data;
    }
}
