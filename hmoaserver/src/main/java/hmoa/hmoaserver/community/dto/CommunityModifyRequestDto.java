package hmoa.hmoaserver.community.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CommunityModifyRequestDto {
    private String title;
    private String content;
    private List<Long> deleteCommunityPhotoIds = new ArrayList<>();
}
