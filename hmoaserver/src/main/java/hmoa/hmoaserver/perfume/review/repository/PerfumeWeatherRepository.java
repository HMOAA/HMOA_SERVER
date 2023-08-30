package hmoa.hmoaserver.perfume.review.repository;

import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.perfume.domain.Perfume;
import hmoa.hmoaserver.perfume.review.domain.PerfumeWeather;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PerfumeWeatherRepository extends JpaRepository<PerfumeWeather, Long> {
    Optional<PerfumeWeather> findByMemberAndPerfume(Member member , Perfume perfume);
}
