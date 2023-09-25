package hmoa.hmoaserver.community.service;

import hmoa.hmoaserver.community.domain.Community;
import hmoa.hmoaserver.community.domain.CommunityComment;
import hmoa.hmoaserver.community.dto.CommunityCommentAllResponseDto;
import hmoa.hmoaserver.community.dto.CommunityCommentDefaultRequestDto;
import hmoa.hmoaserver.community.dto.CommunityCommentDefaultResponseDto;
import hmoa.hmoaserver.member.domain.Member;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CommunityCommentService {
    Page<CommunityComment> getCommunityComment(Long communityId, int pageNum);
    Page<CommunityComment> getCommunityComment(Long communityId, int pageNum, Member member);
    CommunityComment saveCommunityComment(Member member, CommunityCommentDefaultRequestDto dto, Community community);
}
