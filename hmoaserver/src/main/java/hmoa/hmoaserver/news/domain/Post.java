package hmoa.hmoaserver.news.domain;

import hmoa.hmoaserver.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


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

    @Builder
    public Post(String title, String subtitle, String tag, String content){
        this.title=title;
        this.subtitle=subtitle;
        this.tag=tag;
        this.content=content;
    }
}
