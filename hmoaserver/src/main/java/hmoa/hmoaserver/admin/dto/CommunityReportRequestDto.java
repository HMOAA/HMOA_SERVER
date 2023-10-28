package hmoa.hmoaserver.admin.dto;

import hmoa.hmoaserver.admin.domain.CommunityReport;
import hmoa.hmoaserver.community.domain.Community;
import lombok.Data;

@Data
public class CommunityReportRequestDto {
    private Long targetId;

    public CommunityReport toEntity(Community community){
        return CommunityReport.builder()
                .community(community)
                .build();
    }
}
