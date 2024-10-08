package hmoa.hmoaserver.member.service;

import hmoa.hmoaserver.exception.Code;
import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.member.domain.MemberInfo;
import hmoa.hmoaserver.member.repository.MemberInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberInfoService {

    private final MemberInfoRepository memberInfoRepository;

    @Transactional
    public MemberInfo save(MemberInfo memberInfo) {
        try {
            return memberInfoRepository.save(memberInfo);
        } catch (Exception e) {
            throw new CustomException(e, Code.SERVER_ERROR);
        }
    }

    @Transactional
    public void delete(MemberInfo memberInfo) {
        try {
            memberInfoRepository.delete(memberInfo);
        } catch (Exception e) {
            throw new CustomException(e, Code.SERVER_ERROR);
        }
    }

    public MemberInfo findByMemberId(Long memberId) {
        return memberInfoRepository.findByMemberId(memberId).orElseThrow(() -> new CustomException(null, Code.MEMBER_INFO_NOT_FOUND));
    }

    public boolean isExistMemberInfo(Long memberId) {
        return memberInfoRepository.findByMemberId(memberId).isPresent();
    }
}
