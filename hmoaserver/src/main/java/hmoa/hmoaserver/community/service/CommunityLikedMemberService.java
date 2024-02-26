package hmoa.hmoaserver.community.service;

import hmoa.hmoaserver.community.domain.Community;
import hmoa.hmoaserver.community.domain.CommunityLikedMember;
import hmoa.hmoaserver.member.domain.Member;

public interface CommunityLikedMemberService {
    boolean isCommunityLikedMember(Member member, Community community);
    CommunityLikedMember save(Member member, Community community);
    void delete(Member member, Community community);
    CommunityLikedMember findOneCommunityLiked(Member member, Community community);
}
