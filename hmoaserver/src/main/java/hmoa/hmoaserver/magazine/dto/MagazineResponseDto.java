package hmoa.hmoaserver.magazine.dto;

import hmoa.hmoaserver.common.DateUtils;
import hmoa.hmoaserver.magazine.domain.Magazine;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class MagazineResponseDto {
    private Long magazineId;
    private String title;
    private String createAt;
    private String preview;
    private String previewImgUrl;
    private List<ContentResponseDto> contents;
    private List<String> tags;
    private int viewCount;
    private int likeCount;
    private boolean liked = false;

    public MagazineResponseDto(Magazine magazine) {
        this.magazineId = magazine.getId();
        this.title = magazine.getTitle();
        this.createAt = DateUtils.extractDate(magazine.getCreatedAt());
        this.contents = magazine.getContents().stream().map(ContentResponseDto::new).collect(Collectors.toList());
        this.tags = magazine.getTags();
        this.viewCount = magazine.getViewCount();
        this.likeCount = magazine.getLikeCount();
        this.preview = magazine.getPreview();
        this.previewImgUrl = magazine.getPreviewImgUrl();
    }

    public MagazineResponseDto(Magazine magazine, boolean liked) {
        this.magazineId = magazine.getId();
        this.title = magazine.getTitle();
        this.createAt = DateUtils.extractDate(magazine.getCreatedAt());
        this.contents = magazine.getContents().stream().map(ContentResponseDto::new).collect(Collectors.toList());
        this.tags = magazine.getTags();
        this.viewCount = magazine.getViewCount();
        this.likeCount = magazine.getLikeCount();
        this.preview = magazine.getPreview();
        this.previewImgUrl = magazine.getPreviewImgUrl();
        this.liked = liked;
    }
}
