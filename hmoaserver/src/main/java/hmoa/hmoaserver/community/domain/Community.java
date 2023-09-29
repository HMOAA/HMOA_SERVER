package hmoa.hmoaserver.community.domain;

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
public class Community extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "community_id")
    private Long id;

    private String title;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    private Category category;

    @OneToMany(mappedBy = "community", cascade = CascadeType.ALL , orphanRemoval = true)
    List<CommunityComment> communityComments = new ArrayList<>();
    @Builder
    public Community(String title, String content, Member member, Category category){
        this.title = title;
        this.content = content;
        this.category = category;
        this.member = member;
    }

    public void modifyContent(String content){
        this.content = content;
    }

    public boolean isWrited(Member member){
        return this.member.equals(member);
    }
}
