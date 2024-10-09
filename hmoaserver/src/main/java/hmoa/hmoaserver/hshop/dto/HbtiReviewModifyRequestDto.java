package hmoa.hmoaserver.hshop.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class HbtiReviewModifyRequestDto {

    private String content;
    private List<Long> deleteReviewPhotoIds = new ArrayList<>();
}
