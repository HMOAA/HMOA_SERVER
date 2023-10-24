package hmoa.hmoaserver.admin.dto;

import hmoa.hmoaserver.admin.domain.CommunityReport;
import hmoa.hmoaserver.community.domain.Community;
import lombok.Data;

@Data
public class CommunityReportRequestDto {
    private Long targetId;
    private String content;

    public CommunityReport toEntity(Community community){
        return CommunityReport.builder()
                .reportContent(content)
                .community(community)
                .build();
    }
}
