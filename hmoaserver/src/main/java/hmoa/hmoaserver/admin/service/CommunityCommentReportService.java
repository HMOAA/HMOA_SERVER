package hmoa.hmoaserver.admin.service;

import hmoa.hmoaserver.admin.dto.CommunityCommentReportRequestDto;
import hmoa.hmoaserver.admin.repository.CommunityCommentReportRepository;
import hmoa.hmoaserver.community.domain.CommunityComment;
import hmoa.hmoaserver.community.service.CommunityCommentService;
import hmoa.hmoaserver.exception.Code;
import hmoa.hmoaserver.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommunityCommentReportService {
    private final CommunityCommentService communityCommentService;
    private final CommunityCommentReportRepository communityCommentReportRepository;

    public void save(CommunityCommentReportRequestDto dto){
        CommunityComment communityComment = communityCommentService.findOneComunityComment(dto.getTargetId());
        try{
            communityCommentReportRepository.save(dto.toEntity(communityComment));
        }catch (RuntimeException e){
            throw new CustomException(null, Code.SERVER_ERROR);
        }
    }
}
