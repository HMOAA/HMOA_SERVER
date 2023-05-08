package hmoa.hmoaserver.member.dto;

import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.member.domain.ProviderType;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

@ToString
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class MemberResponseDto {
    private Long memberId;
    private String nickname;
    private ProviderType provider;
    private String imgUrl;
    public MemberResponseDto(Member member){
        this.memberId = member.getId();
        this.nickname= member.getNickname();
        this.provider=member.getProviderType();
        if(member.getImgUrl() != null){
            this.imgUrl=member.getImgUrl();
        }
    }
}
