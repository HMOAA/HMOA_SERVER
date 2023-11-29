package hmoa.hmoaserver.fcm.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import hmoa.hmoaserver.fcm.NotificationType;
import hmoa.hmoaserver.fcm.dto.FCMNotificationRequestDto;
import hmoa.hmoaserver.fcm.dto.FCMTokenSaveRequestDto;
import hmoa.hmoaserver.fcm.service.constant.NotificationConstants;
import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class FCMNotificationService {
    private final FirebaseMessaging firebaseMessaging;
    private final MemberRepository memberRepository;

    public String sendNotification(FCMNotificationRequestDto requestDto) {
        Optional<Member> member = memberRepository.findById(requestDto.getId());
        String status = "";

        if (member.isEmpty()) {
            return "멤버가 존재하지 않습니다.";
        }
        if (member.get().getFirebaseToken() == null) {
            return "멤버의 토큰이 없습니다.";
        }

        if (requestDto.getType() == NotificationType.COMMUNITY_LIKE) {
            status = sendCommunityLike(member.get());
        }

        return status;
    }

    private String sendCommunityLike(Member member) {
        Message message = makeMessage(member, NotificationConstants.LIKE_ALARM_NOTICE, NotificationConstants.LIKE_COMMUNITY_ALARM_MESSAGE);
        try {
            firebaseMessaging.send(message);
            return "알림 전송 성공";
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }
        return "알림 전송 실패";
    }

    private static Message makeMessage(Member member, String title, String body) {
        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(member.getNickname() + body)
                .build();

        return Message.builder()
                .setToken(member.getFirebaseToken())
                .setNotification(notification)
                .build();
    }

    public String sendNotificationByToken(FCMNotificationRequestDto requestDto) {
        Optional<Member> member = memberRepository.findById(requestDto.getId());

        if (member.isPresent()) {
            if (member.get().getFirebaseToken() != null) {
                Notification notification = Notification.builder()
                        .setTitle(requestDto.getTitle())
                        .setBody(requestDto.getContent())
                        .build();

                Message message = Message.builder()
                        .setToken(member.get().getFirebaseToken())
                        .setNotification(notification)
                        .build();

                try {
                    firebaseMessaging.send(message);
                    return "알림 전송 완료";
                } catch (FirebaseMessagingException e) {
                    e.printStackTrace();
                    return "알림 전송 실패";
                }
            } return "서버에 저장된 해당 유저의 FirebaseToken이 존재하지 않습니다.";
        } return "해당 유저가 존재하지 않습니다.";
    }
}
