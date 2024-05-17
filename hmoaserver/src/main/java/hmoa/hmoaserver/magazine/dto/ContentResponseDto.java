package hmoa.hmoaserver.magazine.dto;

import hmoa.hmoaserver.magazine.domain.MagazineContent;
import lombok.Data;

@Data
public class ContentResponseDto {
    private String type;
    private String data;

    public ContentResponseDto(MagazineContent magazineContent) {
        this.type = magazineContent.getType();
        this.data = magazineContent.getData();
    }
}
