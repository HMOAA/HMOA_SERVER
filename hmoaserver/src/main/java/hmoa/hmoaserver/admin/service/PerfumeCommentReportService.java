package hmoa.hmoaserver.admin.service;

import hmoa.hmoaserver.admin.dto.PerfumeCommentReportRequestDto;
import hmoa.hmoaserver.admin.repository.PerfumeCommentReportRepository;
import hmoa.hmoaserver.exception.Code;
import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.perfume.domain.PerfumeComment;
import hmoa.hmoaserver.perfume.repository.PerfumeCommentRepository;
import hmoa.hmoaserver.perfume.service.PerfumeCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PerfumeCommentReportService {
    private final PerfumeCommentRepository perfumeCommentRepository;
    private final PerfumeCommentReportRepository perfumeCommentReportRepository;

    public void save(PerfumeCommentReportRequestDto dto){
        PerfumeComment perfumeComment = perfumeCommentRepository.findById(dto.getTargetId()).orElseThrow(() -> new CustomException(null, Code.COMMENT_NOT_FOUND));
        try {
            perfumeCommentReportRepository.save(dto.toEntity(perfumeComment));
        }catch (RuntimeException e){
            throw new CustomException(null,Code.SERVER_ERROR);
        }
    }
}
