package hmoa.hmoaserver.fcm.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import hmoa.hmoaserver.common.DateUtils;
import hmoa.hmoaserver.common.PageSize;
import hmoa.hmoaserver.exception.Code;
import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.fcm.domain.PushAlarm;
import hmoa.hmoaserver.fcm.dto.FCMNotificationRequestDto;
import hmoa.hmoaserver.fcm.dto.FCMTestRequestDto;
import hmoa.hmoaserver.fcm.repository.PushAlarmRepository;
import hmoa.hmoaserver.fcm.service.constant.NotificationConstants;
import hmoa.hmoaserver.fcm.service.constant.NotificationMessage;
import hmoa.hmoaserver.fcm.service.constant.NotificationMessageFactory;
import hmoa.hmoaserver.fcm.service.constant.NotificationType;
import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static hmoa.hmoaserver.fcm.service.constant.NotificationConstants.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class FCMNotificationService {
    private final FirebaseMessaging firebaseMessaging;
    private final MemberService memberService;
    private final PushAlarmRepository pushAlarmRepository;

    public void sendNotification(FCMNotificationRequestDto requestDto) {
        log.info("알림 보내기");
        Optional<Member> member = memberService.findById(requestDto.getReceiverId());
        Optional<Member> sender = memberService.findById(requestDto.getSenderId());

        if (!isValidNotification(requestDto, member)) {
            return;
        }

        NotificationMessage notificationMessage = NotificationMessageFactory.getMessage(requestDto.getType());

        PushAlarm pushAlarm = savePushAlarm(notificationMessage, member.get(), sender.get(), requestDto.getTargetId());

        Message message = makeMessage(pushAlarm, member.get().getFirebaseToken());

        sendMessage(message);
    }

    public PushAlarm testNotification(FCMTestRequestDto requestDto) {
        log.info("테스트 알림");
        Member testMember = memberService.findById(0l).get();

        NotificationMessage notificationMessage = NotificationMessageFactory.getMessage(requestDto.getType());
        PushAlarm pushAlarm = savePushAlarm(notificationMessage, testMember, testMember, 40l);
        Message message = makeMessage(pushAlarm, requestDto.getToken());
        sendMessage(message);

        return pushAlarm;
    }

    public PushAlarm testNotification(FCMTestRequestDto requestDto, String token) {
        log.info("테스트 알림 (저장까지)");
        Member receiver = memberService.findByMember(token);
        Member testMember = memberService.findById(0l).get();

        NotificationMessage notificationMessage = NotificationMessageFactory.getMessage(requestDto.getType());
        PushAlarm pushAlarm = savePushAlarm(notificationMessage, receiver, testMember, requestDto.getTargetId());
        Message message = makeMessage(pushAlarm, requestDto.getToken());
        sendMessage(message);

        return pushAlarm;
    }

    @Transactional
    public PushAlarm savePushAlarm(NotificationMessage message, Member receiver, Member sender, Long targetId) {
        PushAlarm pushAlarm = PushAlarm.builder()
                .member(receiver)
                .title(message.getTitle())
                .content(message.getContent(sender.getNickname()))
                .deeplink(message.getDeeplinkUrl(targetId))
                .senderProfileImgUrl(sender.getMemberPhoto().getPhotoUrl())
                .build();

        return pushAlarmRepository.save(pushAlarm);
    }

    @Transactional(readOnly = true)
    public PushAlarm findById(Long id) {
        return pushAlarmRepository.findById(id).orElseThrow(() -> new CustomException(null, Code.PUSH_ALARM_NOT_FOUND));
    }


    @Transactional(readOnly = true)
    public Page<PushAlarm> findPushAlarms(Member member) {
        return pushAlarmRepository.findAllByMemberOrderByCreatedAtDesc(member, PageRequest.of(PageSize.ZERO_PAGE.getSize(), PageSize.TEN_SIZE.getSize()));
    }

    @Transactional
    public void readPushAlarm(PushAlarm pushAlarm) {
        pushAlarm.read();
    }

    private static boolean isValidNotification(FCMNotificationRequestDto requestDto, Optional<Member> member) {
        if (isSameMember(requestDto.getReceiverId(), requestDto.getSenderId())) {
            return false;
        }

        return member.filter(value -> value.getFirebaseToken() != null).isPresent();

    }

    private static Message makeMessage(PushAlarm pushAlarm, String token) {
        Notification notification = Notification.builder()
                .setTitle(pushAlarm.getTitle())
                .setBody(pushAlarm.getContent())
                .build();

        return Message.builder()
                .setToken(token)
                .setNotification(notification)
                .putData(PUSH_ALARM_ID_TITLE, String.valueOf(pushAlarm.getId()))
                .putData(TITLE_TITLE, pushAlarm.getTitle())
                .putData(CONTENT_TITLE, pushAlarm.getContent())
                .putData(CREATED_AT_TITLE, DateUtils.extractAlarmDate(pushAlarm.getCreatedAt()))
                .putData(IS_READ_TITLE, String.valueOf(pushAlarm.isRead()))
                .putData(DEEPLINK_TITLE, pushAlarm.getDeeplink())
                .putData(SENDER_PROFILE_TITLE, pushAlarm.getSenderProfileImgUrl())
                .build();
    }

    private void sendMessage(Message message) {
        try {
            firebaseMessaging.send(message);
            log.info("푸쉬 알림 성공");
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
            log.info("푸쉬 알림 실패");
        }
    }

    private static boolean isSameMember(Long receiverId, Long senderId) {
        return receiverId.equals(senderId);
    }

}
