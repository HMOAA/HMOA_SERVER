package hmoa.hmoaserver.note.repository;

import hmoa.hmoaserver.note.domain.DetailNote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetailNoteRepository extends JpaRepository<DetailNote, Long> {
}
