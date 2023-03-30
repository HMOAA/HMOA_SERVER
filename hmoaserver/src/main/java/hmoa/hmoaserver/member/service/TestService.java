package hmoa.hmoaserver.member.service;

import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = false)
@RequiredArgsConstructor
public class TestService {
    private final MemberRepository memberRepository;

    @Transactional
    public Member save(Member member){
        try{
            return memberRepository.save(member);
        }catch (RuntimeException e){
            throw new RuntimeException(e);
        }
    }

    public void createTest(){
        for(int i = 0; i<50; i++){
            Member member= Member.builder()
                    .email("qkr"+i)
                    .build();
            memberRepository.save(member);
        }
    }

    public Page<Member> findTest(){
        try{
            return memberRepository.findAll(Pageable.ofSize(10));
        }catch (RuntimeException e){
            throw new RuntimeException();
        }
    }
}
