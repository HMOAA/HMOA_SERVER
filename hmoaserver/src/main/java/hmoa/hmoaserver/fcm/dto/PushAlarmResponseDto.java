package hmoa.hmoaserver.fcm.dto;

import hmoa.hmoaserver.common.DateUtils;
import hmoa.hmoaserver.fcm.domain.PushAlarm;
import lombok.Data;

@Data
public class PushAlarmResponseDto {
    private long id;
    private String title;
    private String content;
    private String deeplink;
    private String createdAt;
    private boolean isRead;

    public PushAlarmResponseDto(PushAlarm alarm, String category) {
        this.id = alarm.getId();
        this.title = alarm.getTitle();
        this.content = alarm.getContent();
        this.deeplink = alarm.getDeeplink();
        this.createdAt = DateUtils.extractAlarmDate(alarm.getCreatedAt());
        this.isRead = alarm.isRead();
    }
}
