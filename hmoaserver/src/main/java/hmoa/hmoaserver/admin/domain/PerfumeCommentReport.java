package hmoa.hmoaserver.admin.domain;

import hmoa.hmoaserver.perfume.domain.PerfumeComment;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PerfumeCommentReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "perfume_comment_report_id")
    private Long id;

    private String reportContent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "perfume_comment_id")
    private PerfumeComment perfumeComment;

    @Builder
    public PerfumeCommentReport(String reportContent, PerfumeComment perfumeComment){
        this.reportContent = reportContent;
        this.perfumeComment = perfumeComment;
    }
}
