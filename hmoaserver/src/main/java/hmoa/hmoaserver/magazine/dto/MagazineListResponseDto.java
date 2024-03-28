package hmoa.hmoaserver.magazine.dto;

import hmoa.hmoaserver.magazine.domain.Magazine;
import lombok.Data;

@Data
public class MagazineListResponseDto {
    private String title;
    private String preview;
    private String previewImgUrl;

    public MagazineListResponseDto(Magazine magazine) {
        this.title = magazine.getTitle();
        this.preview = magazine.getPreview();
        this.previewImgUrl = magazine.getPreviewImgUrl();
    }
}
