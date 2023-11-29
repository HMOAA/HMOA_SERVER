package hmoa.hmoaserver.member.repository;

import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.member.domain.ProviderType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>{
    Optional<Member> findByEmail(String email);

    Optional<Member> findByNickname(String nickname);

    Boolean existsByNickname(String nickname);

    Optional<Member> findByRefreshToken(String refreshToken);

    Optional<Member> findByemailAndProviderType(String email,ProviderType providerType);

    @Modifying
    @Query("UPDATE Member m " +
            "SET m.firebaseToken = null " +
            "WHERE m.id = :memberId")
    int updateFirebaseTokenToNull(@Param("memberId") Long memberId);
}
