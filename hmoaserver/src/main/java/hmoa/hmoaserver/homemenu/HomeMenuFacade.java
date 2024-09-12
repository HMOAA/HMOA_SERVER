package hmoa.hmoaserver.homemenu;

import hmoa.hmoaserver.common.PageUtil;
import hmoa.hmoaserver.homemenu.domain.HomeMenu;
import hmoa.hmoaserver.homemenu.domain.PerfumeHomeMenu;
import hmoa.hmoaserver.homemenu.dto.HomeMenuAllResponseDto;
import hmoa.hmoaserver.homemenu.dto.HomeMenuDefaultResponseDto;
import hmoa.hmoaserver.homemenu.dto.HomeMenuFirstResponseDto;
import hmoa.hmoaserver.homemenu.dto.HomeMenuPerfumeResponseDto;
import hmoa.hmoaserver.homemenu.service.HomeMenuCachingService;
import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.member.service.MemberService;
import hmoa.hmoaserver.perfume.domain.Perfume;
import hmoa.hmoaserver.perfume.service.PerfumeCachingService;
import hmoa.hmoaserver.perfume.service.PerfumeLikedMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static hmoa.hmoaserver.homemenu.controller.constant.MainPageConstant.*;

@Component
@RequiredArgsConstructor
public class HomeMenuFacade {

    private final HomeMenuCachingService homeMenuCachingService;
    private final PerfumeCachingService perfumeCachingService;
    private final MemberService memberService;
    private final PerfumeLikedMemberService perfumeLikedMemberService;

    public HomeMenuFirstResponseDto getHomeMenuFirst() {
        HomeMenu homeMenu = homeMenuCachingService.getHomeMenu(BRAND_HOME.getId());
        String banner = homeMenuCachingService.getHomeMenu(BANNER.getId()).getTitle();
        List<PerfumeHomeMenu> perfumeHomeMenus = homeMenuCachingService.getPerfumeHomeMenus(homeMenu);
        List<Perfume> perfumes = getPerfumes(perfumeHomeMenus);
        List<HomeMenuPerfumeResponseDto> perfumeResponseDtos =
                perfumes.stream().map(HomeMenuPerfumeResponseDto::new).collect(Collectors.toList());
        return new HomeMenuFirstResponseDto(new HomeMenuDefaultResponseDto(homeMenu.getTitle(), perfumeResponseDtos), banner);
    }

    public List<HomeMenuDefaultResponseDto> getHomeMenuSecond() {
        HomeMenu firstMenu = homeMenuCachingService.getHomeMenu(FIRST_HOME.getId());
        HomeMenu secondMenu = homeMenuCachingService.getHomeMenu(SECOND_HOME.getId());
        HomeMenu thirdMenu = homeMenuCachingService.getHomeMenu(THIRD_HOME.getId());

        List<Perfume> firstPerfumes = getPerfumes(homeMenuCachingService.getPerfumeHomeMenus(firstMenu));
        List<Perfume> secondPerfumes = getPerfumes(homeMenuCachingService.getPerfumeHomeMenus(secondMenu));
        List<Perfume> thirdPerfumes = getPerfumes(homeMenuCachingService.getPerfumeHomeMenus(thirdMenu));

        HomeMenuDefaultResponseDto firstResult = new HomeMenuDefaultResponseDto(firstMenu.getTitle(), getPerfumeResponseDto(firstPerfumes));
        HomeMenuDefaultResponseDto secondResult = new HomeMenuDefaultResponseDto(firstMenu.getTitle(), getPerfumeResponseDto(secondPerfumes));
        HomeMenuDefaultResponseDto thirdResult = new HomeMenuDefaultResponseDto(firstMenu.getTitle(), getPerfumeResponseDto(thirdPerfumes));

        return List.of(firstResult, secondResult, thirdResult);
    }

    public List<HomeMenuAllResponseDto> getHomeMenuAllFirst(String token) {
        HomeMenu homeMenu = homeMenuCachingService.getHomeMenu(FIRST_HOME.getId());
        return getHomeMenuResult(homeMenu, token);
    }

    public List<HomeMenuAllResponseDto> getHomeMenuAllSecond(String token) {
        HomeMenu homeMenu = homeMenuCachingService.getHomeMenu(SECOND_HOME.getId());
        return getHomeMenuResult(homeMenu, token);
    }

    public List<HomeMenuAllResponseDto> getHomeMenuAllThird(String token) {
        HomeMenu homeMenu = homeMenuCachingService.getHomeMenu(THIRD_HOME.getId());
        return getHomeMenuResult(homeMenu, token);
    }


    private List<Perfume> getPerfumes(List<PerfumeHomeMenu> perfumeHomeMenus) {
        return perfumeHomeMenus.stream()
                .map(perfumeHomeMenu -> perfumeCachingService.getPerfumeById(perfumeHomeMenu.getPerfumeId()))
                .toList();
    }

    private List<HomeMenuAllResponseDto> getHomeMenuResult(HomeMenu homeMenu, String token) {
        List<Perfume> perfumes = getPerfumes(homeMenuCachingService.getPerfumeHomeMenus(homeMenu));
        if (memberService.isTokenNullOrEmpty(token)) {
            return perfumes.stream().map(HomeMenuAllResponseDto::new).collect(Collectors.toList());
        }
        Member member = memberService.findByMember(token);
        return getResultForMember(perfumes, member);
    }

    private List<HomeMenuAllResponseDto> getResultForMember(List<Perfume> perfumes, Member member) {
        return perfumes.stream().map(perfume -> {
            boolean isLiked = perfumeLikedMemberService.isMemberLikedPerfume(member, perfume);
            return new HomeMenuAllResponseDto(perfume, isLiked);
        }).toList();
    }

    private static List<HomeMenuPerfumeResponseDto> getPerfumeResponseDto(List<Perfume> perfumes) {
        return convertListToPage(perfumes).stream().map(HomeMenuPerfumeResponseDto::new).collect(Collectors.toList());
    }

    private static Page<Perfume> convertListToPage(List<Perfume> perfumes) {
        return new PageUtil<Perfume>().convertListToPage(perfumes, 0, 5);
    }
}
