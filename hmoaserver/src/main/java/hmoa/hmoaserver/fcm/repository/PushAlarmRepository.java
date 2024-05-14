package hmoa.hmoaserver.fcm.repository;

import hmoa.hmoaserver.fcm.domain.PushAlarm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PushAlarmRepository extends JpaRepository<PushAlarm, Long> {
}
