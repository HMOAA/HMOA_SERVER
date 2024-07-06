package hmoa.hmoaserver.note.repository;

import hmoa.hmoaserver.note.domain.NoteDetailNote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteDetailNoteRepository extends JpaRepository<NoteDetailNote, Long> {
}
