package hmoa.hmoaserver.admin.dto;

import hmoa.hmoaserver.admin.domain.PerfumeCommentReport;
import hmoa.hmoaserver.perfume.domain.PerfumeComment;
import lombok.Data;

@Data
public class PerfumeCommentReportRequestDto {
    private Long targetId;
    private String content;

    public PerfumeCommentReport toEntity(PerfumeComment perfumeComment){
        return PerfumeCommentReport.builder()
                .perfumeComment(perfumeComment)
                .reportContent(content)
                .build();
    }
}
