package hmoa.hmoaserver.photo.repository;

import hmoa.hmoaserver.photo.domain.HbtiPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HbtiPhotoRepository extends JpaRepository<HbtiPhoto, Long> {

    @Modifying
    @Query("DELETE FROM HbtiPhoto p WHERE p.id IN :deleteIds")
    void deleteByIds(@Param("deleteIds") List<Long> hbtiPhotos);
}
