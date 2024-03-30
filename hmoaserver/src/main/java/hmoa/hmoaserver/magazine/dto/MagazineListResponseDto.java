package hmoa.hmoaserver.magazine.dto;

import hmoa.hmoaserver.magazine.domain.Magazine;
import lombok.Data;

@Data
public class MagazineListResponseDto {
    private Long magazineId;
    private String title;
    private String preview;
    private String previewImgUrl;

    public MagazineListResponseDto(Magazine magazine) {
        this.magazineId = magazine.getId();
        this.title = magazine.getTitle();
        this.preview = magazine.getPreview();
        this.previewImgUrl = magazine.getPreviewImgUrl();
    }
}
