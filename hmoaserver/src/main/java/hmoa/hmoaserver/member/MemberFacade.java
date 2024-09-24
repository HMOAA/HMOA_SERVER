package hmoa.hmoaserver.member;

import hmoa.hmoaserver.community.domain.Community;
import hmoa.hmoaserver.community.domain.CommunityComment;
import hmoa.hmoaserver.community.domain.CommunityCommentLikedMember;
import hmoa.hmoaserver.community.dto.CommunityByCategoryResponseDto;
import hmoa.hmoaserver.community.dto.CommunityCommentByMemberResponseDto;
import hmoa.hmoaserver.community.service.CommunityCommentLikedMemberService;
import hmoa.hmoaserver.community.service.CommunityCommentService;
import hmoa.hmoaserver.community.service.CommunityService;
import hmoa.hmoaserver.exception.Code;
import hmoa.hmoaserver.exception.CustomException;
import hmoa.hmoaserver.hshop.domain.OrderEntity;
import hmoa.hmoaserver.hshop.dto.OrderInfoResponseDto;
import hmoa.hmoaserver.hshop.service.NoteProductService;
import hmoa.hmoaserver.hshop.service.OrderService;
import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.member.domain.Role;
import hmoa.hmoaserver.member.dto.*;
import hmoa.hmoaserver.member.service.MemberAddressService;
import hmoa.hmoaserver.member.service.MemberInfoService;
import hmoa.hmoaserver.member.service.MemberService;
import hmoa.hmoaserver.perfume.domain.PerfumeComment;
import hmoa.hmoaserver.perfume.domain.PerfumeCommentLiked;
import hmoa.hmoaserver.perfume.dto.PerfumeCommentByMemberResponseDto;
import hmoa.hmoaserver.perfume.service.PerfumeCommentLikedMemberService;
import hmoa.hmoaserver.perfume.service.PerfumeCommentService;
import hmoa.hmoaserver.photo.service.MemberPhotoService;
import hmoa.hmoaserver.photo.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MemberFacade {

    private final MemberService memberService;
    private final OrderService orderService;
    private final PhotoService photoService;
    private final MemberPhotoService memberPhotoService;
    private final CommunityService communityService;
    private final CommunityCommentService communityCommentService;
    private final PerfumeCommentService perfumeCommentService;
    private final PerfumeCommentLikedMemberService perfumeCommentLikedMemberService;
    private final CommunityCommentLikedMemberService commentLikedMemberService;
    private final MemberAddressService memberAddressService;
    private final MemberInfoService memberInfoService;
    private final NoteProductService noteProductService;

    @Value("${default.profile}")
    private String DEFAULT_PROFILE;

    private static final int SHIPPING_FEE = 3000;

    public MemberResponseDto getOneMember(String token) {
        Member member = memberService.findByMember(token);

        if (member.getRole() == Role.GUEST) {
            throw new CustomException(null, Code.MEMBER_NOT_FOUND);
        }

        return new MemberResponseDto(member);
    }

    public MemberResponseDto joinMember(String token, JoinUpdateRequestDto dto) {
        Member member = memberService.findByMember(token);

        memberService.joinMember(member, dto.getAge(), dto.isSex(), dto.getNickname());

        return new MemberResponseDto(member);
    }

    public void updateNickname(String token, NicknameRequestDto dto) {
        Member member = memberService.findByMember(token);

        memberService.updateNickname(member, dto.getNickname());
    }

    public boolean checkNicknameDuplicate(NicknameRequestDto dto) {
        return memberService.isExistingNickname(dto.getNickname());
    }

    public void updateAge(String token, AgeRequestDto dto) {
        Member member = memberService.findByMember(token);

        memberService.updateAge(member, dto.getAge());
    }

    public void updateSex(String token, SexRequestDto dto) {
        Member member = memberService.findByMember(token);

        memberService.updateSex(member, dto.isSex());
    }

    public List<PerfumeCommentByMemberResponseDto> getMyPerfumeComments(String token, int page) {
        Member member = memberService.findByMember(token);
        Page<PerfumeComment> comments = perfumeCommentService.findPerfumeCommentByMember(member, page);

        return comments.stream()
                .map(comment -> new PerfumeCommentByMemberResponseDto(comment, perfumeCommentLikedMemberService.isMemberLikedPerfumeComment(member, comment), true))
                .toList();
    }

    public List<CommunityCommentByMemberResponseDto> getMyCommunityComments(String token, int page) {
        Member member = memberService.findByMember(token);
        Page<CommunityComment> comments = communityCommentService.findAllCommunityCommentByMember(member, page);

        return comments.stream()
                .map(comment -> new CommunityCommentByMemberResponseDto(comment, commentLikedMemberService.isCommentLikedMember(member, comment), true))
                .toList();
    }

    public List<PerfumeCommentByMemberResponseDto> getMyPerfumeCommentsByHearts(String token, int page) {
        Member member = memberService.findByMember(token);
        Page<PerfumeCommentLiked> commentLikeds = perfumeCommentLikedMemberService.findAllByMember(member, page);

        return commentLikeds.stream()
                .map(commentLiked -> new PerfumeCommentByMemberResponseDto(commentLiked.getPerfumeComment(), true, member.isSameMember(commentLiked.getMember())))
                .toList();
    }

    public List<CommunityCommentByMemberResponseDto> getMyCommunityComentsByHearts(String token, int page) {
        Member member = memberService.findByMember(token);
        Page<CommunityCommentLikedMember> commentLikeds = commentLikedMemberService.findAllByMember(member, page);

        return commentLikeds.stream()
                .map(commentLiked -> new CommunityCommentByMemberResponseDto(commentLiked.getCommunityComment(), commentLiked.getMember().isSameMember(member), commentLiked.getCommunityComment().isWrited(member)))
                .toList();
    }

    public List<CommunityByCategoryResponseDto> getMyCommunities(String token, int page) {
        Member member = memberService.findByMember(token);
        Page<Community> communities = communityService.getCommunityByMember(member, page);
        return communities.stream()
                .map(CommunityByCategoryResponseDto::new)
                .toList();
    }

    public void saveMemberPhoto(String token, MultipartFile file) {
        Member member = memberService.findByMember(token);

        photoService.validateFileExistence(file);
        memberService.saveMemberPhoto(member, file);
    }

    public void deleteMemberPhoto(String token) {
        Member member = memberService.findByMember(token);

        memberPhotoService.validateMemberPhotoIsExistence(member);
        memberPhotoService.delete(member.getMemberPhoto());
    }

    public void deleteMember(String token) {
        Member member = memberService.findByMember(token);
    }

    public void saveMemberAddress(String token, MemberAddressSaveRequestDto dto) {
        Member member = memberService.findByMember(token);

        memberAddressService.save(dto.toEntity(member));
    }

    public void saveOrderInfo(String token, MemberInfoRequestDto dto) {
        Member member = memberService.findByMember(token);

        if (memberInfoService.isExistMemberInfo(member.getId())) {
            memberInfoService.delete(memberInfoService.findByMemberId(member.getId()));
        }

        memberInfoService.save(dto.toEntity(member));
    }

    public MemberInfoResponseDto getOrderInfo(String token) {
        Member member = memberService.findByMember(token);

        return new MemberInfoResponseDto(memberInfoService.findByMemberId(member.getId()));
    }

    public MemberAddressResponseDto getAddress(String token) {
        Member member = memberService.findByMember(token);

        return new MemberAddressResponseDto(memberAddressService.findByMemberId(member.getId()));
    }

    public List<MemberOrderResponseDto> getMemberOrders(String token) {
        Member member = memberService.findByMember(token);

        List<OrderEntity> orders = orderService.findByMemberId(member.getId());
        return orders.stream()
                .map(order -> new MemberOrderResponseDto(
                        order, new OrderInfoResponseDto(noteProductService.getNoteProducts(order.getProductIds()), order.getTotalPrice(), SHIPPING_FEE)))
                .toList();
    }
}
