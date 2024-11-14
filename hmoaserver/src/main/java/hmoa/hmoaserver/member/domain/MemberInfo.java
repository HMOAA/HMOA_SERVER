package hmoa.hmoaserver.member.domain;

import hmoa.hmoaserver.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberInfo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_info_id")
    private Long id;

    private String name;
    private String phoneNumber;

    @Column(name = "member_id")
    private Long memberId;

    @Builder
    public MemberInfo(String name, String phoneNumber, Long memberId) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.memberId = memberId;
    }
}
