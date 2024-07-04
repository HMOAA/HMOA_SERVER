package hmoa.hmoaserver.note.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class DetailNote {

    @Id
    @Column(name = "detail_note_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;
    private String content;

    @OneToMany(mappedBy = "detailNote", cascade = CascadeType.ALL , orphanRemoval = true)
    private List<NoteDetailNote> noteDetailNotes = new ArrayList<>();
}
