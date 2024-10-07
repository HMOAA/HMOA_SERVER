package hmoa.hmoaserver.photo.repository;

import hmoa.hmoaserver.photo.domain.HbtiPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HbtiPhotoRepository extends JpaRepository<HbtiPhoto, Long> {
}
