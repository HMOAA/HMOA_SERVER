package hmoa.hmoaserver.note.repository;


import hmoa.hmoaserver.note.domain.NoteSynonym;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NoteSynonymRepository extends JpaRepository<NoteSynonym, Long> {
    Optional<NoteSynonym> findByNoteName(String noteName);
}
