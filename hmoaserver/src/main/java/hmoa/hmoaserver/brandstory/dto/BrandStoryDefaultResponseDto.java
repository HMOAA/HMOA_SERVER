package hmoa.hmoaserver.brandstory.dto;

import hmoa.hmoaserver.brandstory.domain.BrandStory;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BrandStoryDefaultResponseDto {
    private Long brandStoryId;
    private String brandStoryTitle;
    private String brandStorySubtitle;

    public BrandStoryDefaultResponseDto(BrandStory brandStory) {
        this.brandStoryId = brandStory.getId();
        this.brandStoryTitle = brandStory.getTitle();
        this.brandStorySubtitle = brandStory.getSubtitle();
    }
}
