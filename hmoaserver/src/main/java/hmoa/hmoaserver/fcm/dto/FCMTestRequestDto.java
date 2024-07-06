package hmoa.hmoaserver.fcm.dto;

import hmoa.hmoaserver.fcm.service.constant.NotificationType;
import lombok.Builder;
import lombok.Data;

@Data
public class FCMTestRequestDto {
    private NotificationType type;
    private String token;
    private Long targetId;
}
