package hmoa.hmoaserver.admin.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class TrackingCallbackRequestDto {

    private String carrierId;
    private String trackingNumber;
}
