package hmoa.hmoaserver.photo.service;

import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.perfume.domain.Perfume;
import hmoa.hmoaserver.photo.domain.PerfumePhoto;
import hmoa.hmoaserver.photo.repository.PerfumePhotoRepository;
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
public class PerfumePhotoService {

    @Value("${cloud.aws.s3.bucket-name.perfume}")
    private String perfumePhotoBucketName;
    private final PhotoService photoService;
    private final PerfumePhotoRepository perfumePhotoRepository;

    /**
     * DB에 PerfumePhoto 저장 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public PerfumePhoto save(PerfumePhoto photo) {
        try {
            return perfumePhotoRepository.save(photo);
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * 향수 사진 저장 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public PerfumePhoto savePerfumePhotos(Perfume perfume, MultipartFile file) {

        String folderName = perfume.getKoreanName() + "/" + "perfume-" + perfume.getId();
        String fileName = UUID.randomUUID() + file.getContentType().replace("image/", ".");

        String photoUrl = photoService.insertFileToS3(perfumePhotoBucketName, folderName, fileName, file);

        PerfumePhoto savedPerfumePhoto = null;

        try {
            savedPerfumePhoto = PerfumePhoto.builder()
                    .perfume(perfume)
                    .photoUrl(photoUrl)
                    .build();
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
        return save(savedPerfumePhoto);
    }

}
