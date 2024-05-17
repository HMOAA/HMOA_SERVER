package hmoa.hmoaserver.fcm.dto;

import hmoa.hmoaserver.common.DateUtils;
import hmoa.hmoaserver.fcm.domain.PushAlarm;
import lombok.Data;

@Data
public class PushAlarmResponseDto {
    private String category;
    private String content;
    private String createdAt;
    private boolean isRead;

    public PushAlarmResponseDto(PushAlarm alarm) {
        this.category = alarm.getAlarmCategory().name();
        this.content = alarm.getContent();
        this.createdAt = DateUtils.extractAlarmDate(alarm.getCreatedAt());
        this.isRead = alarm.isRead();
    }
}
