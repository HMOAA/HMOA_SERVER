package hmoa.hmoaserver.admin.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class WebhookInput {
    private String carrierId = "kr.cjlogistics";
    private String trackingNumber;
    private String callbackUrl;
    private String expirationTime;

    public WebhookInput(String trackingNumber, String callbackUrl, String expirationTime) {
        this.trackingNumber = trackingNumber;
        this.callbackUrl = callbackUrl;
        this.expirationTime = expirationTime;
    }
}
