package hmoa.hmoaserver.note.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NoteDetailNote {

    @Id
    @Column(name = "note_detail_note_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "note_id")
    private Note note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "detail_note_id")
    private DetailNote detailNote;

    @Builder
    public NoteDetailNote(Note note, DetailNote detailNote) {
        this.note = note;
        this.detailNote = detailNote;
    }
}
