package hmoa.hmoaserver.hshop.domain;

import hmoa.hmoaserver.note.domain.Note;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NoteProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "note_product_id")
    private Long id;

    private int price;

    @ManyToOne
    @JoinColumn(name = "note_id")
    private Note note;
}
