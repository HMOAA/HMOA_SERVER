package hmoa.hmoaserver.oauth.userinfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OAuth2UserDto {
    private String email;
    private String name;

}