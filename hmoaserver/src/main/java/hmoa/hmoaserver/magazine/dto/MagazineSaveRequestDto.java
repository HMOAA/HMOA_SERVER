package hmoa.hmoaserver.magazine.dto;

import hmoa.hmoaserver.magazine.domain.Magazine;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class MagazineSaveRequestDto {
    private String title;
    private String preview;
    private List<ContentRequestDto> contents;
    private List<String> tags;

    public Magazine toEntity() {
        return Magazine.builder()
                .title(title)
                .tags(tags)
                .build();
    }
}
