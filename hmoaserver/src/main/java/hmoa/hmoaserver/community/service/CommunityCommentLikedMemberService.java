package hmoa.hmoaserver.community.service;

import hmoa.hmoaserver.community.domain.CommunityComment;
import hmoa.hmoaserver.community.domain.CommunityCommentLikedMember;
import hmoa.hmoaserver.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommunityCommentLikedMemberService {
    boolean isCommentLikedMember(Member member, CommunityComment comment);
    CommunityCommentLikedMember save(Member member, CommunityComment comment);
    void delete(Member member, CommunityComment comment);
    CommunityCommentLikedMember findOneCommentLiked(Member member, CommunityComment comment);
    Page<CommunityCommentLikedMember> findAllByMember(Member member, int page);
    Page<CommunityCommentLikedMember> findAllByMemberAndCursor(Member member, Long cursor);
}
