package hmoa.hmoaserver.fcm.dto;

import hmoa.hmoaserver.common.DateUtils;
import hmoa.hmoaserver.fcm.domain.PushAlarm;
import lombok.Data;

@Data
public class PushAlarmResponseDto {
    private String category;
    private String target;
    private String content;
    private String createdAt;
    private long parentId;
    private boolean isRead;

    public PushAlarmResponseDto(PushAlarm alarm, String category) {
        this.target = alarm.getAlarmCategory().name();
        this.category = category;
        this.content = alarm.getContent();
        this.createdAt = DateUtils.extractAlarmDate(alarm.getCreatedAt());
        this.parentId = alarm.getParentId();
        this.isRead = alarm.isRead();
    }
}
