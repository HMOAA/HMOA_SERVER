package hmoa.hmoaserver.community.repository;

import hmoa.hmoaserver.community.domain.Category;
import hmoa.hmoaserver.community.domain.Community;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityRepository extends JpaRepository<Community, Long> {
    Page<Community> findAllByCategoryOrderByCreatedAtDescIdAsc(Category category, Pageable pageable);
    Page<Community> findAllByOrderByCreatedAtDescIdAsc(Pageable pageable);
    Page<Community> findByCategoryAndTitleContainingOrContentContainingOrderByCreatedAtDescIdAsc(
            Category category, String title, String content, Pageable pageable
    );
    Page<Community> findByTitleContainingOrContentContainingOrderByCreatedAtDescIdAsc(
            String title, String content, Pageable pageable
    );
}
