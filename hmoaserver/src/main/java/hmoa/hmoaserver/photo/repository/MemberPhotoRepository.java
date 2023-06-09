package hmoa.hmoaserver.photo.repository;

import hmoa.hmoaserver.photo.domain.MemberPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberPhotoRepository extends JpaRepository<MemberPhoto, Long> {
}
