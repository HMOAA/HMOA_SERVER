package hmoa.hmoaserver.photo.service;

import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.photo.domain.MemberPhoto;
import hmoa.hmoaserver.photo.repository.MemberPhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

import static hmoa.hmoaserver.exception.Code.FILE_NOT_FOUND;
import static hmoa.hmoaserver.exception.Code.SERVER_ERROR;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberPhotoService {

    @Value("${cloud.aws.s3.bucket-name.member}")
    private String memberPhotoBucketName;
    private final PhotoService photoService;
    private final MemberPhotoRepository memberPhotoRepository;

    /**
     * DB에 MemberPhoto 저장 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public MemberPhoto save(MemberPhoto photo) {
        try {
            return memberPhotoRepository.save(photo);
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * 멤버 사진 저장 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public MemberPhoto saveMemberPhotos(Member member, MultipartFile file) {

        String folderName = member.getUsername() + "/" + "member-" + member.getId();
        String fileName = UUID.randomUUID() + file.getContentType().replace("image/", ".");

        String memberPhotoUrl = photoService.insertFileToS3(memberPhotoBucketName, folderName, fileName, file);

        MemberPhoto savedMemberPhoto = null;

        try {
            savedMemberPhoto = MemberPhoto.builder()
                    .member(member)
                    .photoUrl(memberPhotoUrl)
                    .build();
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }

        return save(savedMemberPhoto);
    }

    /**
     * 회원 사진 삭제 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public void delete(MemberPhoto memberPhoto) {
        try {
            memberPhoto.delete();
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * 회원 사진 존재 여부 검증 |
     * 404(FILE_NOT_FOUND)
     */
    public void validateMemberPhotoIsExistence(Member member) {
        if (member.getMemberPhoto() == null)
            throw new CustomException(null, FILE_NOT_FOUND);
    }

}
