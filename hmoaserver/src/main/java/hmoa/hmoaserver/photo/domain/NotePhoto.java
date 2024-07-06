package hmoa.hmoaserver.photo.domain;

import hmoa.hmoaserver.note.domain.Note;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotePhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "note_photo_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "note_id")
    private Note note;

    private String photoUrl;

    @Builder
    public NotePhoto(Note note, String photoUrl) {
        this.note = note;
        this.photoUrl = photoUrl;
    }
}
