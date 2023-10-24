package hmoa.hmoaserver.admin.dto;

import hmoa.hmoaserver.admin.domain.CommunityCommentReport;
import hmoa.hmoaserver.community.domain.CommunityComment;
import lombok.Data;

@Data
public class CommunityCommentReportRequestDto {
    private Long targetId;
    private String content;

    public CommunityCommentReport toEntity(CommunityComment communityComment){
        return CommunityCommentReport.builder()
                .communityComment(communityComment)
                .reportContent(content)
                .build();
    }
}
