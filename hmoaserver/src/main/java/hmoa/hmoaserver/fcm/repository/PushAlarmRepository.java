package hmoa.hmoaserver.fcm.repository;

import hmoa.hmoaserver.fcm.domain.PushAlarm;
import hmoa.hmoaserver.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PushAlarmRepository extends JpaRepository<PushAlarm, Long> {
    Page<PushAlarm> findAllByMemberOrderByCreatedAtDesc(Member member, Pageable pageable);
}
