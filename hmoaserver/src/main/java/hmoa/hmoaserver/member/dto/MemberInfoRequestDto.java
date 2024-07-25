package hmoa.hmoaserver.member.dto;

import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.member.domain.MemberInfo;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberInfoRequestDto {

    private String name;
    private String phoneNumber;

    public MemberInfo toEntity(Member member) {
        return MemberInfo.builder()
                .name(name)
                .phoneNumber(phoneNumber)
                .member(member)
                .build();
    }
}
