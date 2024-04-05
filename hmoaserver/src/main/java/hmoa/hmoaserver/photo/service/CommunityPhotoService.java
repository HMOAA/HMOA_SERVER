package hmoa.hmoaserver.photo.service;

import hmoa.hmoaserver.community.domain.Community;
import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.photo.domain.CommunityPhoto;
import hmoa.hmoaserver.photo.repository.CommunityPhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static hmoa.hmoaserver.exception.Code.COMMUNITYPHOTO_NOT_FOUND;
import static hmoa.hmoaserver.exception.Code.SERVER_ERROR;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommunityPhotoService {

    @Value("${cloud.aws.s3.bucket-name.community}")
    private String communityBucketName;
    private final PhotoService photoService;
    private final CommunityPhotoRepository communityPhotoRepository;

    /**
     * DB에 CommunityPhoto 저장 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public CommunityPhoto save(CommunityPhoto photo) {
        try {
            return communityPhotoRepository.save(photo);
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * 커뮤니티 게시글 사진 저장 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public List<CommunityPhoto> saveCommunityPhotos(Community community, List<MultipartFile> files) {

        String folderName = "community-" + community.getId();

        List<CommunityPhoto> savedPhotos = new ArrayList<>();

        for (MultipartFile file : files) {
            String fileName = UUID.randomUUID() + file.getContentType().replace("image/", ".");
            String photoUrl = photoService.insertFileToS3(communityBucketName, folderName, fileName, file);

            CommunityPhoto savedCommunityPhoto = null;

            try {
                savedCommunityPhoto = CommunityPhoto.builder()
                        .community(community)
                        .photoUrl(photoUrl)
                        .build();
            } catch (RuntimeException e) {
                throw new CustomException(e, SERVER_ERROR);
            }
            save(savedCommunityPhoto);

            savedPhotos.add(savedCommunityPhoto);
        }
        return savedPhotos;
    }

    @Transactional
    public CommunityPhoto findById(Long communityId, Long photoId) {
        return communityPhotoRepository.findByIdAndCommunityIdAndIsDeletedIsFalse(photoId, communityId)
                .orElseThrow(() -> {
                    throw new CustomException(null, COMMUNITYPHOTO_NOT_FOUND);
                });
    }
}
