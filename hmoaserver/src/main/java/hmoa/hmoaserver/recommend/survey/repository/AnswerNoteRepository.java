package hmoa.hmoaserver.recommend.survey.repository;

import hmoa.hmoaserver.recommend.survey.domain.AnswerNote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerNoteRepository extends JpaRepository<AnswerNote, Long> {
}
