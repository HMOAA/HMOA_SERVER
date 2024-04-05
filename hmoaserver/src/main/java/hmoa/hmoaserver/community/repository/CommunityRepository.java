package hmoa.hmoaserver.community.repository;

import hmoa.hmoaserver.community.domain.Category;
import hmoa.hmoaserver.community.domain.Community;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommunityRepository extends JpaRepository<Community, Long> {
    Page<Community> findAllByCategoryOrderByCreatedAtDescIdDesc(Category category, Pageable pageable);
    Page<Community> findAllByCategoryOrderByHeartCountDescIdDesc(Category category, Pageable pageable);
    Page<Community> findAllByOrderByCreatedAtDescIdAsc(Pageable pageable);
    Page<Community> findByCategoryAndTitleContainingOrContentContainingOrderByCreatedAtDescIdAsc(
            Category category, String title, String content, Pageable pageable
    );
    Page<Community> findByTitleContainingOrContentContainingOrderByCreatedAtDescIdAsc(
            String title, String content, Pageable pageable
    );

    // 커뮤니티의 카테고리별 최신순 lastCommentId보다 낮은 게시글을 조회
    @Query("SELECT c " +
            "FROM Community c " +
            "WHERE c.id < ?1 AND " +
            "c.category = ?2 " +
            "ORDER BY c.createdAt DESC, c.id Desc")
    Page<Community> findCommunityNextPage(Long lastCommentId, Category category, PageRequest pageRequest);
}
