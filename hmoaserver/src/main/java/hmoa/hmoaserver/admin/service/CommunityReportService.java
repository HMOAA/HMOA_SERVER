package hmoa.hmoaserver.admin.service;

import hmoa.hmoaserver.admin.dto.CommunityReportRequestDto;
import hmoa.hmoaserver.admin.repository.CommunityReportRepository;
import hmoa.hmoaserver.community.domain.Community;
import hmoa.hmoaserver.community.repository.CommunityRepository;
import hmoa.hmoaserver.community.service.CommunityService;
import hmoa.hmoaserver.exception.Code;
import hmoa.hmoaserver.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommunityReportService{
    private final CommunityReportRepository communityReportRepository;
    private final CommunityService communityService;
    public void save(CommunityReportRequestDto dto) {
        Community community = communityService.getCommunityById(dto.getTargetId());
        try{
            communityReportRepository.save(dto.toEntity(community));
        }catch (RuntimeException e){
            throw new CustomException(null, Code.SERVER_ERROR);
        }
    }
}
