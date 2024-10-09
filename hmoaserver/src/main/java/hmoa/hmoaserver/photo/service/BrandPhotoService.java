package hmoa.hmoaserver.photo.service;

import hmoa.hmoaserver.brand.domain.Brand;
import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.photo.domain.BrandPhoto;
import hmoa.hmoaserver.photo.repository.BrandPhotoRepository;
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
public class BrandPhotoService {

    @Value("${cloud.aws.s3.bucket-name.brand}")
    private String brandPhotoBucketName;

    private final PhotoService photoService;
    private final BrandPhotoRepository brandPhotoRepository;

    /**
     * DB에 BrandPhoto 저장 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public BrandPhoto save(BrandPhoto photo) {
        try {
            return brandPhotoRepository.save(photo);
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * 브랜드 사진 저장 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public BrandPhoto saveBrandPhotos(Brand brand, MultipartFile file) {

        String folderName = brand.getBrandName() + "/" + "brand-" + brand.getId();
        String fileName = UUID.randomUUID() + file.getContentType().replace("image/", ".");

        String brandPhotoUrl = photoService.insertFileToS3(brandPhotoBucketName, folderName, fileName, file);

        BrandPhoto savedBrandPhoto = null;

        try {
            savedBrandPhoto = BrandPhoto.builder()
                    .brand(brand)
                    .photoUrl(brandPhotoUrl)
                    .build();
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }

        return save(savedBrandPhoto);
    }

    @Transactional
    public BrandPhoto saveS3BrandPhotos(Brand brand) {

        String url = photoService.getPhotoUrl(brandPhotoBucketName, brand.getBrandName());

        return save(BrandPhoto.builder().brand(brand).photoUrl(url).build());
    }
}
