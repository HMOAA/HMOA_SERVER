package hmoa.hmoaserver.member.domain;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_address_id")
    private Long id;

    private String name;
    private String addressName;
    private String phoneNumber;
    private String landlineNumber;
    private String zipCode;
    private String streetAddress;
    private String detailAddress;
    private String request;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public MemberAddress(String name, String addressName, String phoneNumber, String landlineNumber, String zipCode, String streetAddress, String detailAddress, String request, Member member) {
        this.name = name;
        this.addressName = addressName;
        this.phoneNumber = phoneNumber;
        this.landlineNumber = landlineNumber;
        this.zipCode = zipCode;
        this.streetAddress = streetAddress;
        this.detailAddress = detailAddress;
        this.request = request;
        this.member = member;
    }
}
