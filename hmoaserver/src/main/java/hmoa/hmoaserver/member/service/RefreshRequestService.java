package hmoa.hmoaserver.member.service;

import hmoa.hmoaserver.exception.Code;
import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.member.domain.RefreshRequest;
import hmoa.hmoaserver.member.repository.RefreshRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RefreshRequestService {
    private final RefreshRequestRepository refreshRequestRepository;

    @Transactional
    public RefreshRequest save(Member member, String previousToken, String token) {
        try {
            return refreshRequestRepository.save(RefreshRequest.builder()
                    .member(member)
                    .previousRefreshToken(previousToken)
                    .refreshToken(token)
                    .build());
        } catch (RuntimeException e) {
            throw new CustomException(null, Code.SERVER_ERROR);
        }
    }
}
