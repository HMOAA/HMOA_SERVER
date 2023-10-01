package hmoa.hmoaserver.admin.service;

import hmoa.hmoaserver.admin.domain.PerfumeByHome;
import hmoa.hmoaserver.admin.dto.PerfumeByHomeSaveRequestDto;
import hmoa.hmoaserver.member.domain.Member;

public interface PerfumeByHomeService {
    PerfumeByHome save(Member member, PerfumeByHomeSaveRequestDto dto);
}
