package hmoa.hmoaserver.term.repository;

import hmoa.hmoaserver.term.domain.Term;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TermRepository  extends JpaRepository<Term, Long> {
}