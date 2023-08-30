package hmoa.hmoaserver.perfume.review.domain;

import hmoa.hmoaserver.perfume.domain.Perfume;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Table(name = "PerfumeReview")
@AllArgsConstructor
public class PerfumeReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "perfume_review_id")
    private Long id;
    private int spring;
    private int summer;
    private int autumn;
    private int winter;
    private int ten;
    private int twenty;
    private int thirty;
    private int fourty;
    private int fifty;
    private int man;
    private int woman;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "perfume_id")
    private Perfume perfume;
}
