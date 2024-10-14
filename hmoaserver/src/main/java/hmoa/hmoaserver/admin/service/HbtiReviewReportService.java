package hmoa.hmoaserver.admin.service;

import hmoa.hmoaserver.admin.domain.HbtiReviewReport;
import hmoa.hmoaserver.admin.repository.HbtiReviewReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class HbtiReviewReportService implements ReportService<HbtiReviewReport> {

    private final HbtiReviewReportRepository hbtiReviewReportRepository;

    @Override
    public JpaRepository<HbtiReviewReport, Long> getReportRepository() {
        return hbtiReviewReportRepository;
    }
}
