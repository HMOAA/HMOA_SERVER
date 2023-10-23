package hmoa.hmoaserver.note.repository;

import hmoa.hmoaserver.note.domain.Note;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository<Note, Long> {
}
