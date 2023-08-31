package hmoa.hmoaserver.perfume.review.repository;

import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.perfume.domain.Perfume;
import hmoa.hmoaserver.perfume.review.domain.PerfumeGender;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PerfumeGenderRepository extends JpaRepository<PerfumeGender, Long> {
    Optional<PerfumeGender> findByMemberAndPerfume(Member member , Perfume perfume);
}
