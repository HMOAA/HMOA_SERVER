package hmoa.hmoaserver.fcm.service.constant;

import hmoa.hmoaserver.member.domain.Member;

public interface NotificationMessage {
    String getTitle();
    String getContent(String sender);
    String getDeeplinkUrl(long targetId);
    default String getSenderProfileImgUrl(Member member) {
        return member.getMemberPhoto().getPhotoUrl();
    }
}
