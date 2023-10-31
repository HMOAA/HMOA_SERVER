package hmoa.hmoaserver.admin.dto;

import hmoa.hmoaserver.admin.domain.CommunityCommentReport;
import hmoa.hmoaserver.community.domain.CommunityComment;
import lombok.Data;

@Data
public class CommunityCommentReportRequestDto {
    private Long targetId;

    public CommunityCommentReport toEntity(CommunityComment communityComment){
        return CommunityCommentReport.builder()
                .communityComment(communityComment)
                .build();
    }
}
