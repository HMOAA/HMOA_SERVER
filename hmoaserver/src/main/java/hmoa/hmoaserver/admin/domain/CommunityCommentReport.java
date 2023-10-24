package hmoa.hmoaserver.admin.domain;

import hmoa.hmoaserver.community.domain.CommunityComment;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommunityCommentReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "community_comment_report_id")
    private Long id;

    private String reportContent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community_comment_id")
    private CommunityComment communityComment;

    @Builder
    public CommunityCommentReport(CommunityComment communityComment, String reportContent){
        this.reportContent = reportContent;
        this.communityComment = communityComment;
    }
}
