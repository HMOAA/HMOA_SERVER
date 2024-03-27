package hmoa.hmoaserver.community.domain;

import hmoa.hmoaserver.admin.domain.CommunityCommentReport;
import hmoa.hmoaserver.common.BaseEntity;
import hmoa.hmoaserver.member.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommunityComment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "community_comment_id")
    private Long id;

    private String content;

    private int heartCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "community_id")
    private Community community;

    @OneToMany(mappedBy = "communityComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommunityCommentReport> communityCommentReports = new ArrayList<>();

    @OneToMany(mappedBy = "communityComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommunityCommentLikedMember> commentLikedMembers = new ArrayList<>();

    @Builder
    public CommunityComment(Member member, String content,Community community){
        this.content=content;
        this.member=member;
        this.community=community;
        this.heartCount = 0;
    }

    public boolean isWrited(Member member){
        return this.member.equals(member);
    }

    public void modifyComment(String content){
        this.content = content;
    }

    public void increaseHeartCount() {
        this.heartCount += 1;
    }

    public void decreaseHeartCount() {
        this.heartCount -= 1;
    }

    public void setCommunityIsNull() {
        this.community = null;
    }
}
