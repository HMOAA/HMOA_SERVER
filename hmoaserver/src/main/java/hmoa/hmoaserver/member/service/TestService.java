package hmoa.hmoaserver.member.service;

import hmoa.hmoaserver.brand.domain.Brand;
import hmoa.hmoaserver.brand.repository.BrandRepository;
import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.member.repository.MemberRepository;
import hmoa.hmoaserver.perfume.domain.Perfume;
import hmoa.hmoaserver.perfume.repository.PerfumeRepository;
import hmoa.hmoaserver.photo.domain.PerfumePhoto;
import hmoa.hmoaserver.photo.repository.PerfumePhotoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = false)
@RequiredArgsConstructor
public class TestService {
    @Value("${defalut.maintest}")
    private String DEFALUT_MAINTEST;

    private final MemberRepository memberRepository;
    private final PerfumePhotoRepository perfumePhotoRepository;
    private final PerfumeRepository perfumeRepository;
    private final BrandRepository brandRepository;

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

    public void perfumeTest(){
        Brand brand = brandRepository.findById(Long.parseLong("1")).get();
        for(int i = 0; i<20; i++){
            Perfume perfume = Perfume.builder()
                    .brand(brand)
                    .englishName("jomalon")
                    .koreanName("조말론")
                    .build();
            perfumeRepository.save(perfume);
            PerfumePhoto photo = PerfumePhoto.builder()
                    .perfume(perfume)
                    .photoUrl(DEFALUT_MAINTEST)
                    .build();
            perfumePhotoRepository.save(photo);
        }
    }

    public Page<Perfume> perfumeFindTest(){
        try{
            return perfumeRepository.findAll(Pageable.ofSize(5));
        }catch (RuntimeException e){
            throw new RuntimeException();
        }
    }

    public Page<Perfume> perfumeFindTest2(){
        try{
            return perfumeRepository.findAll(Pageable.ofSize(15));
        }catch (RuntimeException e){
            throw new RuntimeException();
        }
    }
}
