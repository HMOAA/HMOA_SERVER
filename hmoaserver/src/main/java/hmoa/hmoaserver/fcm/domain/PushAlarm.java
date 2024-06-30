package hmoa.hmoaserver.fcm.domain;

import hmoa.hmoaserver.common.BaseEntity;
import hmoa.hmoaserver.member.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PushAlarm extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "push_alarm_id")
    private long id;
    private String title;
    private String content;
    private String deeplink;
    private String senderProfileImgUrl;
    private boolean isRead;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "member_id")
    private Member member;

    @Builder
    public PushAlarm(String title, String content, String deeplink, Member member, String senderProfileImgUrl) {
        this.title = title;
        this.deeplink = deeplink;
        this.content = content;
        this.member = member;
        this.senderProfileImgUrl = senderProfileImgUrl;
        this.isRead = false;
    }

    public void read() {
        this.isRead = true;
    }
}
