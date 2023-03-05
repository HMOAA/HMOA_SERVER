package hmoa.hmoaserver.news.domain;

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
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="post_id")
    private Long id;

    private String title;
    private String subtitle;
    private String tag;
    private String content;

    @ManyToOne(cascade = CascadeType.MERGE, targetEntity = Member.class)
    @JoinColumn(name="member_id")
    private Member member;

    @OneToMany(mappedBy = "post")
    private List<PostComment> comments = new ArrayList<>();

    @Builder
    public Post(String title, String subtitle, String tag, String content,Member member){
        this.title=title;
        this.subtitle=subtitle;
        this.tag=tag;
        this.content=content;
        this.member=member;
    }
}
