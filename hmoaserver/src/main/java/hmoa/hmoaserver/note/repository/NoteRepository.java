package hmoa.hmoaserver.note.repository;

import hmoa.hmoaserver.community.domain.Category;
import hmoa.hmoaserver.note.domain.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note, Long> {
    Page<Note> findAll(Pageable pageable);
    Optional<Note> findByTitle(String title);
    Page<Note> findByTitleContainingOrSubtitleContainingOrderByCreatedAtDesc(
            String title, String subtitle, Pageable pageable
    );

    Page<Note> findAllByOrderByIdDesc(Pageable pageable);

    @Query("SELECT n " +
            "FROM Note n " +
            "WHERE n.id < ?1 " +
            "ORDER BY n.createdAt DESC, n.id Desc")
    Page<Note> findNoteNextPage(Long lastCommentId, PageRequest pageRequest);

    @Query("SELECT n FROM Note n LEFT JOIN FETCH n.noteDetailNotes ndn LEFT JOIN FETCH ndn.detailNote WHERE n.id = :noteId")
    Optional<Note> findByIdWithDetails(@Param("noteId") Long noteId);
}
