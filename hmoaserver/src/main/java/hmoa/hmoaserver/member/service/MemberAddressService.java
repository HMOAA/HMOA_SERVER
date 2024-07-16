package hmoa.hmoaserver.member.service;

import hmoa.hmoaserver.exception.Code;
import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.member.domain.MemberAddress;
import hmoa.hmoaserver.member.repository.MemberAddressRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberAddressService {

    private final MemberAddressRepository memberAddressRepository;

    @Transactional
    public MemberAddress save(MemberAddress memberAddress) {
        try {
            return memberAddressRepository.save(memberAddress);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(e, Code.SERVER_ERROR);
        }
    }
}
