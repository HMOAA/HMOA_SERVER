package hmoa.hmoaserver.brandstory.repository;

import hmoa.hmoaserver.brandstory.domain.BrandStory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BrandStoryRepository extends JpaRepository<BrandStory, Long> {
    Page<BrandStory> findAll(Pageable pageable);

    Page<BrandStory> findByTitleContainingOrSubtitleContainingOrderByCreatedAtDesc(
            String title, String subtitle, Pageable pageable
    );

    Page<BrandStory> findAllByOrderByIdDesc(Pageable pageable);

    @Query("SELECT b " +
            "FROM BrandStory b " +
            "WHERE b.id < ?1 " +
            "ORDER BY b.createdAt DESC, b.id Desc")
    Page<BrandStory> findBrandStoryNextPage(Long lastCommentId, PageRequest pageRequest);
}
