package hmoa.hmoaserver.member.dto;

import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.member.domain.ProviderType;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@ToString
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class MemberResponseDto {
    private Long memberId;
    private String nickname;
    private int age;
    private boolean sex;
    private ProviderType provider;
    private String memberImageUrl;

    public MemberResponseDto(Member member){
        this.memberId = member.getId();
        this.nickname= member.getNickname();
        this.provider=member.getProviderType();
        if(member.getMemberPhoto() != null){
            this.memberImageUrl = member.getMemberPhoto().getPhotoUrl();
        }
        this.age=member.getAge();
        this.sex=member.isSex();
    }
}
