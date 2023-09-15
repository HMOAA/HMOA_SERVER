package hmoa.hmoaserver.community.repository;

import hmoa.hmoaserver.community.domain.Community;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityRepository extends JpaRepository<Community, Long> {
}
