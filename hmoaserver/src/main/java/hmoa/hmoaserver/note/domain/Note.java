package hmoa.hmoaserver.note.domain;

import hmoa.hmoaserver.common.BaseEntity;
import hmoa.hmoaserver.photo.domain.NotePhoto;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Table(name = "Note")
@AllArgsConstructor
public class Note extends BaseEntity {
    @Id
    @Column(name = "note_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;
    private String subtitle;

    private String content;
    private boolean isDeleted = false;

    @OneToMany(mappedBy = "note", cascade = CascadeType.ALL , orphanRemoval = true)
    private List<NotePhoto> notePhotos = new ArrayList<>();

    public void updateContent(String content) {
        this.content = content;
    }

    public void delete() {
        isDeleted = true;
    }

    public NotePhoto getNotePhoto() {
        int size = this.notePhotos.size();

        if (size == 0) {
            return null;
        }

        return notePhotos.get(notePhotos.size() - 1);
    }

}
