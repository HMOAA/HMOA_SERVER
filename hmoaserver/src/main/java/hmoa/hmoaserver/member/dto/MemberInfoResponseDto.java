package hmoa.hmoaserver.member.dto;

import hmoa.hmoaserver.member.domain.MemberInfo;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberInfoResponseDto {

    private String name;
    private String phoneNumber;

    public MemberInfoResponseDto(MemberInfo memberInfo) {
        if (memberInfo != null) {
            this.name = memberInfo.getName();
            this.phoneNumber = memberInfo.getPhoneNumber();
        }
    }
}
