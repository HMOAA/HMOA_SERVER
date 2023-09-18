package hmoa.hmoaserver.community.service;

import hmoa.hmoaserver.community.domain.Community;
import hmoa.hmoaserver.community.domain.CommunityComment;
import hmoa.hmoaserver.community.dto.CommunityCommentDefaultRequestDto;
import hmoa.hmoaserver.member.domain.Member;
import org.springframework.data.domain.Page;

public interface CommunityCommentService {
    Page<CommunityComment> getCommunityComment(Long communityId, int pageNum);
    CommunityComment saveCommunityComment(Member member, CommunityCommentDefaultRequestDto dto, Community community);
}
