package hmoa.hmoaserver.photo.repository;

import hmoa.hmoaserver.photo.domain.MagazinePhoto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MagazinePhotoRepository extends JpaRepository<MagazinePhoto, Long> {
}
