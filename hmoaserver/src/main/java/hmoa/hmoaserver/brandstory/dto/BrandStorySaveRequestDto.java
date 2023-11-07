package hmoa.hmoaserver.brandstory.dto;

import hmoa.hmoaserver.brandstory.domain.BrandStory;
import lombok.Data;

@Data
public class BrandStorySaveRequestDto {
    private String brandStoryTitle;
    private String brandStorySubtitle;
    private String content;

    public BrandStory toEntity() {
        return BrandStory.builder()
                .title(brandStoryTitle)
                .subtitle(brandStorySubtitle)
                .content(content)
                .build();
    }
}
