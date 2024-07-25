package hmoa.hmoaserver.member.repository;

import hmoa.hmoaserver.member.domain.MemberInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberInfoRepository extends JpaRepository<MemberInfo, Integer> {
}
