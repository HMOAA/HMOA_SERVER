package hmoa.hmoaserver.fcm.service.constant;

import hmoa.hmoaserver.exception.Code;
import hmoa.hmoaserver.exception.CustomException;

public class NotificationMessageFactory {
    public static NotificationMessage getMessage(NotificationType type) {
        switch (type) {
            case COMMUNITY_LIKE:
                return new CommunityLikeNotificationMessage();
            case COMMUNITY_COMMENT:
                return new CommunityAddCommentNotificationMessage();
            case COMMUNITY_COMMENT_LIKE:
                return new CommunityCommentLikeNotificationMessage();
            case PERFUME_COMMENT_LIKE:
                return new PerfumeCommentLikeNotificationMessage();
            case HBTI_REVIEW_LIKE:
                return new HbtiReviewLikeNotificationMessage();
            default:
                throw new CustomException(null, Code.SERVER_ERROR);
        }

    }
}
