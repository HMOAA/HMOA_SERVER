package hmoa.hmoaserver.note.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NoteSynonym {

    @Id
    @Column(name = "note_synonym_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String noteName;

    @ElementCollection
    private List<String> synonyms;

    @Builder
    public NoteSynonym(final List<String> synonyms, final String noteName) {
        this.synonyms = synonyms;
        this.noteName = noteName;
    }
}
