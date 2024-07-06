package hmoa.hmoaserver.fcm.service.constant;

public class CommunityAddCommentNotificationMessage implements NotificationMessage{
    @Override
    public String getTitle() {
        return NotificationConstants.ADD_COMMENT_ALARM_TITLE;
    }

    @Override
    public String getContent(String sender) {
        return sender + NotificationConstants.ADD_COMMENT_ALARM_MESSAGE;
    }

    @Override
    public String getDeeplinkUrl(long targetId) {
        return String.format(NotificationConstants.URI_MAPPING, NotificationLink.community, targetId);
    }
}
