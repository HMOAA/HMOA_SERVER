package hmoa.hmoaserver.note.repository;

import hmoa.hmoaserver.note.domain.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository<Note, Long> {
    Page<Note> findAll(Pageable pageable);
    Page<Note> findByTitleContainingOrSubtitleContainingOrderByCreatedAtDesc(
            String title, String subtitle, Pageable pageable
    );
}
