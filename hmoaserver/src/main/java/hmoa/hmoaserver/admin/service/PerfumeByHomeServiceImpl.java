package hmoa.hmoaserver.admin.service;

import hmoa.hmoaserver.admin.domain.PerfumeByHome;
import hmoa.hmoaserver.admin.dto.PerfumeByHomeSaveRequestDto;
import hmoa.hmoaserver.admin.repository.PerfumeByHomeRepository;
import hmoa.hmoaserver.exception.Code;
import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.member.domain.Role;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PerfumeByHomeServiceImpl implements PerfumeByHomeService{
    private final PerfumeByHomeRepository perfumeByHomeRepository;

    @Override
    public PerfumeByHome save(Member member, PerfumeByHomeSaveRequestDto dto) {
        isMemberAdmin(member);
        try {
            return perfumeByHomeRepository.save(dto.toEntity());
        }catch (RuntimeException e){
            throw new CustomException(null,Code.SERVER_ERROR);
        }
    }

    private void isMemberAdmin(Member member){
        if(member.getRole() != Role.ADMIN){
            throw new CustomException(null, Code.FORBIDDEN_AUTHORIZATION);
        }
        return;
    }
}
