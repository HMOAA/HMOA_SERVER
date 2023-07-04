package hmoa.hmoaserver.perfume.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class PerfumeCommentGetResponseDto {
    private Long commentCount;
    private List<PerfumeCommentResponseDto> comments;
}
