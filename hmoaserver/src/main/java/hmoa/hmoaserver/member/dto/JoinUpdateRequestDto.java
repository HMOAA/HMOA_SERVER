package hmoa.hmoaserver.member.dto;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JoinUpdateRequestDto {
    private int age;
    private String sex;
    private String nickname;
}
