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

    @Enumerated(EnumType.STRING)
    private AlarmCategory alarmCategory;
    private long parentId;
    private String content;
    private boolean isRead;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "member_id")
    private Member member;

    @Builder
    public PushAlarm(AlarmCategory alarmCategory, String content, Member member, long parentId) {
        this.alarmCategory = alarmCategory;
        this.parentId = parentId;
        this.content = content;
        this.member = member;
        this.isRead = false;
    }

    public void read() {
        this.isRead = true;
    }
}
