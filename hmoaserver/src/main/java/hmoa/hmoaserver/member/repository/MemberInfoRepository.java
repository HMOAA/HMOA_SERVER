package hmoa.hmoaserver.member.repository;

import hmoa.hmoaserver.member.domain.MemberInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberInfoRepository extends JpaRepository<MemberInfo, Integer> {

    Optional<MemberInfo> findByMemberId(Long memberId);
}
