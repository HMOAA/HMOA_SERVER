package hmoa.hmoaserver.admin.domain;

import hmoa.hmoaserver.common.BaseEntity;
import hmoa.hmoaserver.community.domain.Community;
import hmoa.hmoaserver.community.domain.CommunityComment;
import hmoa.hmoaserver.perfume.domain.PerfumeComment;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommunityReport extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "community_report_id")
    private Long id;

    private String reportContent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community_id")
    private Community community;

    @Builder
    public CommunityReport(Community community, String reportContent){
        this.community = community;
        this.reportContent = reportContent;
    }
}
