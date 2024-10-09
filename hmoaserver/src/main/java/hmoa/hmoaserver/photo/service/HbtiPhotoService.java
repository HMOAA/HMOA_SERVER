package hmoa.hmoaserver.photo.service;

import hmoa.hmoaserver.exception.Code;
import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.hshop.domain.HbtiReview;
import hmoa.hmoaserver.photo.domain.HbtiPhoto;
import hmoa.hmoaserver.photo.repository.HbtiPhotoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HbtiPhotoService {

    @Value("${cloud.aws.s3.bucket-name.hbti}")
    private String hbtiBucketName;

    private final HbtiPhotoRepository hbtiPhotoRepository;
    private final PhotoService photoService;

    @Transactional
    public HbtiPhoto save(HbtiPhoto photo) {
        try {
            return hbtiPhotoRepository.save(photo);
        } catch (Exception e) {
            throw new CustomException(null, Code.SERVER_ERROR);
        }
    }

    @Transactional
    public void deleteAll(List<Long> deleteIds) {
        hbtiPhotoRepository.deleteByIds(deleteIds);
    }

    @Transactional
    public List<HbtiPhoto> savePhotos(HbtiReview hbtiReview, List<MultipartFile> photos) {

        String folderName = "hbti-" + hbtiReview.getId();

        List<HbtiPhoto> savedPhotos = new ArrayList<>();

        for (MultipartFile photo : photos) {
            String fileName = UUID.randomUUID() + photo.getContentType().replace("image/", ".");
            String photoUrl = photoService.insertFileToS3(hbtiBucketName, folderName, fileName, photo);

            HbtiPhoto savedHbtiPhoto = null;

            try {
                savedHbtiPhoto = HbtiPhoto.builder()
                        .hbtiReview(hbtiReview)
                        .photoUrl(photoUrl)
                        .build();
            } catch (RuntimeException e) {
                throw new CustomException(null, Code.SERVER_ERROR);
            }

            save(savedHbtiPhoto);
            savedPhotos.add(savedHbtiPhoto);
        }

        return savedPhotos;
    }
}
