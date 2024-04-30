package hmoa.hmoaserver.magazine.dto;

import hmoa.hmoaserver.community.domain.Community;
import lombok.Data;

@Data
public class TopTastingResponseDto {
    private final Long communityId;
    private final String title;
    private final String profileImg;
    private final String nickname;
    private final String content;

    public TopTastingResponseDto(Community community) {
        this.communityId = community.getId();
        this.title = community.getTitle();
        this.profileImg = community.getMember().getMemberPhoto().getPhotoUrl();
        this.nickname = community.getMember().getNickname();
        this.content = convertContent(community.getContent());
    }

    private static String convertContent(String content) {
        String trimmedContent = content.replaceAll("\\s+", " ").trim();

        if (trimmedContent.length() > 300) {
            trimmedContent = trimmedContent.substring(0, 300);
            int lastSpaceIndex = trimmedContent.lastIndexOf(" ");

            if (lastSpaceIndex != -1) {
                trimmedContent = trimmedContent.substring(0, lastSpaceIndex);
            }

            trimmedContent += "...";
        }
        return trimmedContent;
    }
}
