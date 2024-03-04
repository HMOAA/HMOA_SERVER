package hmoa.hmoaserver.magazine.dto;

import hmoa.hmoaserver.magazine.domain.Magazine;
import lombok.Data;

import java.util.List;

@Data
public class MagazineSaveRequestDto {
    private List<ContentRequestDto> contents;
    private List<String> tags;

    public Magazine toEntity() {
        return Magazine.builder()
                .tags(tags)
                .build();
    }
}
