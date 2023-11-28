package hmoa.hmoaserver.community.repository;

import hmoa.hmoaserver.community.domain.Category;
import hmoa.hmoaserver.community.domain.Community;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommunityRepository extends JpaRepository<Community, Long> {
    Page<Community> findAllByCategoryOrderByCreatedAtDesc(Category category, Pageable pageable);
    Page<Community> findAllByOrderByCreatedAtDesc(Pageable pageable);
    Page<Community> findByCategoryAndTitleContainingOrContentContainingOrderByCreatedAtDesc(
            Category category, String title, String content, Pageable pageable
    );
    Page<Community> findByTitleContainingOrContentContainingOrderByCreatedAtDesc(
            String title, String content, Pageable pageable
    );
}
