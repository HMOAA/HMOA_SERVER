package hmoa.hmoaserver.member.dto;

import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.member.domain.MemberAddress;
import lombok.Data;

import javax.annotation.Nullable;

@Data
public class MemberAddressSaveRequestDto {

    private String name;
    @Nullable
    private String addressName;
    private String phoneNumber;
    private String zipCode;
    private String streetAddress;
    private String detailAddress;
    private String request;

    public MemberAddress toEntity(Member member) {

        return MemberAddress.builder()
                .name(name)
                .addressName(addressName != null ? addressName : "")
                .phoneNumber(phoneNumber)
                .zipCode(zipCode)
                .streetAddress(streetAddress)
                .detailAddress(detailAddress)
                .request(request)
                .member(member)
                .build();
    }
}
