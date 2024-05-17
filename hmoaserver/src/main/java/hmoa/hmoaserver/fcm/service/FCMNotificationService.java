package hmoa.hmoaserver.fcm.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import hmoa.hmoaserver.common.PageSize;
import hmoa.hmoaserver.fcm.domain.AlarmCategory;
import hmoa.hmoaserver.fcm.domain.PushAlarm;
import hmoa.hmoaserver.fcm.dto.FCMNotificationRequestDto;
import hmoa.hmoaserver.fcm.repository.PushAlarmRepository;
import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static hmoa.hmoaserver.fcm.NotificationType.*;
import static hmoa.hmoaserver.fcm.service.constant.NotificationConstants.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class FCMNotificationService {
    private final FirebaseMessaging firebaseMessaging;
    private final MemberRepository memberRepository;
    private final PushAlarmRepository pushAlarmRepository;

    public String sendNotification(FCMNotificationRequestDto requestDto) {
        Optional<Member> member = memberRepository.findById(requestDto.getId());

        if (isSameMember(requestDto.getId(), requestDto.getSenderId())) {
            return SEND_NOT_REQUIRED;
        }

        if (member.isEmpty()) {
            return NOT_FOUND_MEMBER;
        }

        if (member.get().getFirebaseToken() == null) {
            return NOT_FOUND_TOKEN;
        }

        String successControl = "";
        String message = "";
        AlarmCategory category = null;

        if (requestDto.getType() == COMMENT_LIKE) {
            successControl = sendCommentLike(member.get(), requestDto.getSender());
            message = requestDto.getSender() + LIKE_COMMENT_ALARM_MESSAGE;
            category = AlarmCategory.Like;
        } else if (requestDto.getType() == COMMUNITY_LIKE) {
            successControl = sendCommunityLike(member.get(), requestDto.getSender());
            message = requestDto.getSender() + LIKE_COMMUNITY_ALARM_MESSAGE;
            category = AlarmCategory.Like;
        } else if (requestDto.getType() == COMMUNITY_COMMENT) {
            successControl = sendAddComment(member.get(), requestDto.getSender());
            message = requestDto.getSender() + ADD_COMMENT_ALARM_MESSAGE;
            category = AlarmCategory.Comment;
        }

        savePushAlarm(message, category, member.get(), successControl);
        log.info("{}", successControl);
        return successControl;
    }

    @Transactional
    public void savePushAlarm(String message, AlarmCategory category, Member member, String success) {
        if (success.equals(SUCCESS_SEND)) {
            PushAlarm alarm = PushAlarm.builder()
                    .alarmCategory(category)
                    .content(message)
                    .member(member)
                    .build();

            pushAlarmRepository.save(alarm);
        }
    }

    @Transactional(readOnly = true)
    public Page<PushAlarm> findPushAlarms(Member member) {
        return pushAlarmRepository.findAllByMemberOrderByCreatedAtDesc(member, PageRequest.of(PageSize.ZERO_PAGE.getSize(), PageSize.TEN_SIZE.getSize()));
    }

    @Transactional
    public void readPushAlarm(PushAlarm pushAlarm) {
        pushAlarm.read();
    }

    private String sendCommentLike(Member member, String sender) {
        Message message = makeMessage(member, LIKE_ALARM_TITLE, sender + LIKE_COMMENT_ALARM_MESSAGE);
        return send(message);
    }

    private String sendCommunityLike(Member member, String sender) {
        Message message = makeMessage(member, LIKE_ALARM_TITLE, sender + LIKE_COMMUNITY_ALARM_MESSAGE);
        return send(message);
    }

    private String sendAddComment(Member member, String sender) {
        Message message = makeMessage(member, ADD_COMMENT_ALARM_TITLE, sender + ADD_COMMENT_ALARM_MESSAGE);
        return send(message);
    }

    private static Message makeMessage(Member member, String title, String body) {
        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();

        return Message.builder()
                .setToken(member.getFirebaseToken())
                .setNotification(notification)
                .build();
    }

    private String send(Message message) {
        try {
            firebaseMessaging.send(message);
            return SUCCESS_SEND;
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }
        return FAILE_SEND;
    }

    private static boolean isSameMember(Long id, Long senderId) {
        return id.equals(senderId);
    }

}
