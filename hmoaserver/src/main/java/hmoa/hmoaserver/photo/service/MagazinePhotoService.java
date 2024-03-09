package hmoa.hmoaserver.photo.service;

import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.magazine.domain.Magazine;
import hmoa.hmoaserver.photo.domain.MagazinePhoto;
import hmoa.hmoaserver.photo.repository.MagazinePhotoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static hmoa.hmoaserver.exception.Code.SERVER_ERROR;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MagazinePhotoService {
    @Value("${cloud.aws.s3.bucket-name.magazine}")
    private String magazineBucketName;
    private final PhotoService photoService;
    private final MagazinePhotoRepository magazinePhotoRepository;


    @Transactional
    public MagazinePhoto save(MagazinePhoto photo) {
        try {
            return magazinePhotoRepository.save((MagazinePhoto) photo);
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    @Transactional
    public List<MagazinePhoto> saveMagazinePhoto(Magazine magazine, List<MultipartFile> files) {
        String folderName = "magazine-" + magazine.getId();
        List<MagazinePhoto> magazinePhotos = new ArrayList<>();
        files.forEach(file -> {
            String fileName = UUID.randomUUID() + file.getContentType().replace("image/", ".");
            String photoUrl = photoService.insertFileToS3(magazineBucketName, folderName, fileName, file);
            log.info("{}", photoUrl);
            try {
                magazinePhotos.add(save(MagazinePhoto.builder()
                        .magazine(magazine)
                        .photoUrl(photoUrl)
                        .build()));
            } catch (RuntimeException e) {
                throw new CustomException(null, SERVER_ERROR);
            }
        });

        return magazinePhotos;
    }
}
