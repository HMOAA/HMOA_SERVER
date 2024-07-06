package hmoa.hmoaserver.photo.repository;

import hmoa.hmoaserver.photo.domain.NotePhoto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotePhotoRepository extends JpaRepository<NotePhoto, Long> {
}
