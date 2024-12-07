package hmoa.hmoaserver.member.repository;

import hmoa.hmoaserver.member.domain.MemberInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberInfoRepository extends JpaRepository<MemberInfo, Integer> {

    List<MemberInfo> findByMemberId(Long memberId);
}
