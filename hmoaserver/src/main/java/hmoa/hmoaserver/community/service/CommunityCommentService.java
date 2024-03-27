package hmoa.hmoaserver.community.service;

import hmoa.hmoaserver.community.domain.Community;
import hmoa.hmoaserver.community.domain.CommunityComment;
import hmoa.hmoaserver.community.dto.CommunityCommentAllResponseDto;
import hmoa.hmoaserver.community.dto.CommunityCommentDefaultRequestDto;
import hmoa.hmoaserver.community.dto.CommunityCommentDefaultResponseDto;
import hmoa.hmoaserver.community.dto.CommunityCommentModifyRequestDto;
import hmoa.hmoaserver.member.domain.Member;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CommunityCommentService {
    CommunityComment findOneComunityComment(Long commentId);
    Page<CommunityComment> findAllCommunityComment(Long communityId, int pageNum);
    Page<CommunityComment> findAllCommunityComment(Long communityId, Long cursor);
    CommunityComment saveCommunityComment(Member member, CommunityCommentDefaultRequestDto dto, Community community);

    CommunityComment modifyCommunityComment(Member member, CommunityCommentModifyRequestDto dto, Long commentId);

    String deleteCommunityComment(Member member,Long commentId);
}
