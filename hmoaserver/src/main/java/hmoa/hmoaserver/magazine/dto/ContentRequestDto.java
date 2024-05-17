package hmoa.hmoaserver.magazine.dto;

import hmoa.hmoaserver.magazine.domain.MagazineContent;
import hmoa.hmoaserver.magazine.domain.Magazine;
import lombok.Data;

@Data
public class ContentRequestDto {
    private String type;
    private String data;

    public MagazineContent toEntity(Magazine magazine) {
        return MagazineContent.builder()
                .type(type)
                .data(data)
                .magazine(magazine)
                .build();
    }
}
