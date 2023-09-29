package hmoa.hmoaserver.community.dto;

import hmoa.hmoaserver.community.domain.Category;
import hmoa.hmoaserver.community.domain.Community;
import hmoa.hmoaserver.member.domain.Member;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@ToString
@Data
public class CommunityDefaultRequestDto {
    private String title;
    private String content;
//    private Long viewCount;
    @NotBlank(message = "카테고리 선택 필수")
    private Category category;

    public Community toEntity(Member member){
        return Community.builder()
                .title(title)
                .category(category)
                .member(member)
                .content(content)
                .build();
    }
}
