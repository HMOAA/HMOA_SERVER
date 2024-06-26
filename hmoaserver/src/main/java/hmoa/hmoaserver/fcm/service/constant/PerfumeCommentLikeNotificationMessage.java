package hmoa.hmoaserver.fcm.service.constant;

import hmoa.hmoaserver.fcm.NotificationType;

import static hmoa.hmoaserver.fcm.service.constant.NotificationConstants.*;

public class PerfumeCommentLikeNotificationMessage implements NotificationMessage{
    @Override
    public String getTitle() {
        return LIKE_ALARM_TITLE;
    }

    @Override
    public String getContent(String sender) {
        return sender + LIKE_COMMENT_ALARM_MESSAGE;
    }

    @Override
    public String getDeeplinkUrl(long targetId) {
        return String.format(URI_MAPPING, NotificationType.PERFUME_COMMENT_LIKE, targetId);
    }
}
