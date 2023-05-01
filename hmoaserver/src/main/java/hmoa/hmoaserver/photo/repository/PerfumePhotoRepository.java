package hmoa.hmoaserver.photo.repository;

import hmoa.hmoaserver.photo.domain.PerfumePhoto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerfumePhotoRepository extends JpaRepository<PerfumePhoto, Long> {
}
