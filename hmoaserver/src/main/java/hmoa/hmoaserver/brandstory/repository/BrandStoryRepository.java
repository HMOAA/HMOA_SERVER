package hmoa.hmoaserver.brandstory.repository;

import hmoa.hmoaserver.brandstory.domain.BrandStory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandStoryRepository extends JpaRepository<BrandStory, Long> {
}
