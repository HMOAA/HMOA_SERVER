package hmoa.hmoaserver.admin.dto;

import hmoa.hmoaserver.admin.domain.PerfumeCommentReport;
import hmoa.hmoaserver.perfume.domain.PerfumeComment;
import lombok.Data;

@Data
public class PerfumeCommentReportRequestDto {
    private Long targetId;

    public PerfumeCommentReport toEntity(PerfumeComment perfumeComment){
        return PerfumeCommentReport.builder()
                .perfumeComment(perfumeComment)
                .build();
    }
}
