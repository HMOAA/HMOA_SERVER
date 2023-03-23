package hmoa.hmoaserver.member.dto;

import hmoa.hmoaserver.member.domain.Member;
import lombok.*;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberReslutDto {
    private Long memberId;
    private String email;
    private String nickname;
    private String imgUrl;
    private int age;
    private String sex;
    public MemberReslutDto(Member member){
        this.memberId = member.getId();
        this.email = member.getEmail();
        this.nickname= member.getNickname();
        this.age=member.getAge();
        this.sex= member.getSex();
        if(member.getImgUrl() != null){
            this.imgUrl=member.getImgUrl();
        }
    }
}
