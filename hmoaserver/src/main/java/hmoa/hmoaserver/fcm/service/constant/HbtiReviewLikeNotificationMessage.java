package hmoa.hmoaserver.fcm.service.constant;

import static hmoa.hmoaserver.fcm.service.constant.NotificationConstants.*;

public class HbtiReviewLikeNotificationMessage implements NotificationMessage{

    @Override
    public String getTitle() {
        return LIKE_ALARM_TITLE;
    }

    @Override
    public String getContent(String sender) {
        return sender + LIKE_REVIEW_ALARM_MESSAGE;
    }

    @Override
    public String getDeeplinkUrl(long targetId) {
        return String.format(URI_MAPPING, NotificationLink.hbti_review, targetId);
    }
}
