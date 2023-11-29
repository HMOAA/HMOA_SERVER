package hmoa.hmoaserver.fcm.dto;

import hmoa.hmoaserver.fcm.NotificationType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FCMNotificationRequestDto {
    private Long id;
    private NotificationType type;
    private String title;
    private String content;

    @Builder
    public FCMNotificationRequestDto(Long id, String title, String content, NotificationType notificationType) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.type = notificationType;
    }
}
