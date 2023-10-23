package hmoa.hmoaserver.photo.repository;

import hmoa.hmoaserver.photo.domain.CommunityPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityPhotoRepository extends JpaRepository<CommunityPhoto, Long> {
}
