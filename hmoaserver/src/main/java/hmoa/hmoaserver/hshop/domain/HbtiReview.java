package hmoa.hmoaserver.hshop.domain;

import hmoa.hmoaserver.common.BaseEntity;
import hmoa.hmoaserver.photo.domain.HbtiPhoto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HbtiReview extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hbti_review_id")
    private Long id;

    private Long memberId;
    private Long orderId;
    private String content;
    private int heartCount;

    @OneToMany(mappedBy = "hbtiReview", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HbtiPhoto> hbtiPhotos = new ArrayList<>();

    @Builder
    public HbtiReview(Long id, Long memberId, Long orderId, String content) {
        this.id = id;
        this.memberId = memberId;
        this.orderId = orderId;
        this.content = content;
        this.heartCount = 0;
    }
}
