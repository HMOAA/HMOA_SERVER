package hmoa.hmoaserver.magazine.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class ContentRequest {
    @Id
    @Column(name = "content_request_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String type;
    private String data;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "magazine_id", nullable = false)
    private Magazine magazine;
}
