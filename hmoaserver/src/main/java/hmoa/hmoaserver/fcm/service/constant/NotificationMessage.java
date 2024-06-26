package hmoa.hmoaserver.fcm.service.constant;

public interface NotificationMessage {
    String getTitle();
    String getContent(String sender);
    String getDeeplinkUrl(long targetId);
}
