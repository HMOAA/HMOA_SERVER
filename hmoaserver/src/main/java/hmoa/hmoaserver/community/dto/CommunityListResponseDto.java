package hmoa.hmoaserver.community.dto;

import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class CommunityListResponseDto {
    private boolean lastPage;
    private List<CommunityByCategoryResponseDto> communites;

    public CommunityListResponseDto(boolean isLastPage, List<CommunityByCategoryResponseDto> communites) {
        this.lastPage = isLastPage;
        this.communites = communites;
    }
}
