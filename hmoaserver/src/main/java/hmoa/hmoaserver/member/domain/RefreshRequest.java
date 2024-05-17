package hmoa.hmoaserver.member.domain;

import hmoa.hmoaserver.common.BaseEntity;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshRequest extends BaseEntity {
    @Id
    @Column(name = "refresh_request_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String refreshToken;
    private String previousRefreshToken;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "member_id")
    private Member member;

    @Builder
    public RefreshRequest(String previousRefreshToken, String refreshToken, Member member) {
        this.previousRefreshToken = previousRefreshToken;
        this.refreshToken = refreshToken;
        this.member = member;
    }
}
