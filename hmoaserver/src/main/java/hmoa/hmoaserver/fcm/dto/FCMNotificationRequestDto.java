package hmoa.hmoaserver.fcm.dto;

import hmoa.hmoaserver.fcm.NotificationType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FCMNotificationRequestDto {
    private Long id;
    private String sender;
    private NotificationType type;

    @Builder
    public FCMNotificationRequestDto(Long id,String sender, NotificationType notificationType) {
        this.id = id;
        this.sender = sender;
        this.type = notificationType;
    }
}
