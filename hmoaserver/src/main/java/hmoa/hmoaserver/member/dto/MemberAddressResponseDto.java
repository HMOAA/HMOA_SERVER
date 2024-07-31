package hmoa.hmoaserver.member.dto;

import hmoa.hmoaserver.member.domain.MemberAddress;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberAddressResponseDto {

    private String name;
    private String addressName;
    private String phoneNumber;
    private String landlineNumber;
    private String zipCode;
    private String streetAddress;
    private String detailAddress;
    private String request;

    public MemberAddressResponseDto(MemberAddress memberAddress) {
        if (memberAddress != null) {
            this.name = memberAddress.getName();
            this.addressName = memberAddress.getAddressName();
            this.phoneNumber = memberAddress.getPhoneNumber();
            this.landlineNumber = memberAddress.getLandlineNumber();
            this.zipCode = memberAddress.getZipCode();
            this.streetAddress = memberAddress.getStreetAddress();
            this.detailAddress = memberAddress.getDetailAddress();
            this.request = memberAddress.getRequest();
        }
    }
}
