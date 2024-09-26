package hmoa.hmoaserver.admin.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class RegisterTrackingRequestDto {
    private String query = "mutation RegisterTrackWebhook($input: RegisterTrackWebhookInput!) { registerTrackWebhook(input: $input) }";
    private Object variables;

    public RegisterTrackingRequestDto(Object variables) {
        this.variables = variables;
    }
}
