package hmoa.hmoaserver.community.service;

import hmoa.hmoaserver.community.domain.CommunityComment;
import hmoa.hmoaserver.community.domain.CommunityCommentLikedMember;
import hmoa.hmoaserver.member.domain.Member;

public interface CommunityCommentLikedMemberService {
    boolean isCommentLikedMember(Member member, CommunityComment comment);
    CommunityCommentLikedMember save(Member member, CommunityComment comment);
    void delete(Member member, CommunityComment comment);
    CommunityCommentLikedMember findOneCommentLiked(Member member, CommunityComment comment);
}
