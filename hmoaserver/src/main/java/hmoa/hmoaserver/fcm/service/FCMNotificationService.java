package hmoa.hmoaserver.fcm.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import hmoa.hmoaserver.fcm.dto.FCMNotificationRequestDto;
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
