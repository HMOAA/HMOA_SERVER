package hmoa.hmoaserver.perfume.dto;

import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.perfume.domain.PerfumeComment;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PerfumeCommentResponseDto {

    private Long id;
    private String content;
    private int heartCount;
    private String nickname;
    private Long perfumeId;
    private String profileImg;
    private boolean isLiked;
    private boolean isWrited;
    private String createAt;

    public PerfumeCommentResponseDto(PerfumeComment perfumeComment, boolean isLiked, Member member){
        this.id=perfumeComment.getId();
        this.content=perfumeComment.getContent();
        this.heartCount=perfumeComment.getHeartCount();
        this.nickname=perfumeComment.getMember().getNickname();
        this.perfumeId=perfumeComment.getPerfume().getId();
        this.isLiked = isLiked;
        this.createAt=formateDateTime(perfumeComment.getCreatedAt());
        this.profileImg = member.getMemberPhoto().getPhotoUrl();
        if (member==null){
            this.isWrited=false;
        }else{
            if(perfumeComment.getMember().getId()== member.getId()){
                this.isWrited=true;
            }else this.isWrited=false;
        }
    }
    private String formateDateTime(LocalDateTime time){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy/MM/dd HH:mm");
        return time.format(formatter);
    }
}
