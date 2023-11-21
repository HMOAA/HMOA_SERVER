package hmoa.hmoaserver.photo.repository;

import hmoa.hmoaserver.photo.domain.CommunityPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommunityPhotoRepository extends JpaRepository<CommunityPhoto, Long> {
    Optional<CommunityPhoto> findByIdAndCommunityIdAndIsDeletedIsFalse(Long id, Long communityId);
}
