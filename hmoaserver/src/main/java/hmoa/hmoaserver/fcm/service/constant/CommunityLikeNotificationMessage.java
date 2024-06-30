package hmoa.hmoaserver.fcm.service.constant;

import static hmoa.hmoaserver.fcm.service.constant.NotificationConstants.*;

public class CommunityLikeNotificationMessage implements NotificationMessage{
    @Override
    public String getTitle() {
        return LIKE_ALARM_TITLE;
    }

    @Override
    public String getContent(String sender) {
        return sender + LIKE_COMMUNITY_ALARM_MESSAGE;
    }

    @Override
    public String getDeeplinkUrl(long targetId) {
        return String.format(URI_MAPPING, NotificationType.COMMUNITY_LIKE, targetId);
    }
}
