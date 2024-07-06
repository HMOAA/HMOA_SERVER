package hmoa.hmoaserver.photo.service;

import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.note.domain.Note;
import hmoa.hmoaserver.photo.domain.NotePhoto;
import hmoa.hmoaserver.photo.repository.NotePhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

import static hmoa.hmoaserver.exception.Code.SERVER_ERROR;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotePhotoService {

    @Value("${cloud.aws.s3.bucket-name.note}")
    private String brandPhotoBucketName;
    private final PhotoService photoService;
    private final NotePhotoRepository notePhotoRepository;

    /**
     * DB에 NotePhoto 저장 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public NotePhoto save(NotePhoto photo) {
        try {
            return notePhotoRepository.save(photo);
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * 노트 사진 저장 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public NotePhoto saveNotePhoto(Note note, MultipartFile file) {

        String folderName = note.getTitle() + "/" + "note-" + note.getId();
        String fileName = UUID.randomUUID() + file.getContentType().replace("image/", ".");

        String brandPhotoUrl = photoService.insertFileToS3(brandPhotoBucketName, folderName, fileName, file);

        NotePhoto notePhoto = null;

        try {
            notePhoto = notePhoto.builder()
                    .note(note)
                    .photoUrl(brandPhotoUrl)
                    .build();
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }

        return save(notePhoto);
    }
}
