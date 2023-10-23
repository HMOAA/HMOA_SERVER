package hmoa.hmoaserver.photo.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;

import hmoa.hmoaserver.exception.CustomException;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

import static hmoa.hmoaserver.exception.Code.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PhotoService {

    private final AmazonS3Client amazonS3Client;

    /**
     * 파일 형식 검증 |
     * 파일 형식은 .png, .jpg, .jpeg 만 받는다. |
     * 415(FILE_TYPE_UNSUPPORTED)
     */
    public void validateFileType(MultipartFile file) {
        String type = file.getContentType().split("/")[1];

        if (!type.equals("jpg") && !type.equals("jpeg") && !type.equals("png")) {
            throw new CustomException(null, FILE_TYPE_UNSUPPORTED);
        }
    }

    /**
     * 파일 존재 여부 검증 |
     * 404(FILE_NOT_FOUND)
     */
    public void validateFileExistence(MultipartFile file) {
        if (file == null)
            throw new CustomException(null, FILE_NOT_FOUND);
    }

    /**
     * 게시글 첨부 파일 개수 검증 |
     * 413(FILE_COUNT_EXCEED)
     */
    public void validateCommunityPhotoCountExceeded(int count) {
        if(count > 10) {
            throw new CustomException(null, FILE_COUNT_EXCEED);
        }
    }

    /**
     * MultipartFile을 File로 전환 |
     * MultipartFile을 받아서 File의 형태로 전환하여 반환한다.
     */
    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convertingFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        FileOutputStream fileOutputStream = new FileOutputStream(convertingFile);
        fileOutputStream.write(file.getBytes());
        fileOutputStream.close();

        return convertingFile;
    }

    /**
     * S3에 파일 저장 |
     * 파일을 전환하고 특정 파일 관련된 폴더에 파일을 저장하고 URL을 반환한다. |
     * 500(SERVER_ERROR)
     */
    public String insertFileToS3(String bucketName, String folderName, String fileName, MultipartFile file) {

        try {
            File convertedFile = convertMultiPartToFile(file);
            String uploadingFileName = folderName + "/" + fileName;

            amazonS3Client.putObject(new PutObjectRequest(bucketName, uploadingFileName, convertedFile)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
            convertedFile.delete();
            String url = amazonS3Client.getUrl(bucketName, uploadingFileName).toString();
            return url;
        } catch (IOException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * S3에 파일 삭제 |
     * 500(SERVER_ERROR)
     */
    public void deleteFileFromS3(String bucketname, String filePath) {;
        try {
            amazonS3Client.deleteObject(bucketname, filePath);
        } catch (SdkClientException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }
}
