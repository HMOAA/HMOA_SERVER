package hmoa.hmoaserver.hshop.dto;

import hmoa.hmoaserver.common.DateUtils;
import hmoa.hmoaserver.hshop.domain.HbtiReview;
import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.photo.domain.HbtiPhoto;
import hmoa.hmoaserver.photo.dto.PhotoResponseDto;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HbtiReviewResponseDto {

    private Long hbtiReviewId;
    private String profileImgUrl;
    private String author;
    private String content;
    @Setter
    private int imagesCount = 0;
    @Setter
    private List<PhotoResponseDto<HbtiPhoto>> hbtiPhotos = new ArrayList<>();
    private String createdAt;
    private boolean isWrited = false;
    private int heartCount;
    private boolean isLiked = false;
    private String orderTitle;

    @Builder
    public HbtiReviewResponseDto(HbtiReview hbtiReview, String orderTitle, Member author, boolean isWrited, boolean isLiked) {
        this.hbtiReviewId = hbtiReview.getId();
        this.profileImgUrl = author.getMemberPhoto().getPhotoUrl();
        this.author = author.getNickname();
        this.content = hbtiReview.getContent();
        this.createdAt = DateUtils.calculateDaysAgo(hbtiReview.getCreatedAt());
        this.heartCount = hbtiReview.getHeartCount();
        this.orderTitle = orderTitle;
        this.isWrited = isWrited;
        this.isLiked = isLiked;

        if (!hbtiReview.getHbtiPhotos().isEmpty()) {
            List<HbtiPhoto> hbtiPhotoList = hbtiReview.getHbtiPhotos();
            this.imagesCount = hbtiPhotoList.size();

            hbtiPhotoList.forEach(hbtiPhoto -> {
                this.hbtiPhotos.add(new PhotoResponseDto<>(hbtiPhoto));
                log.info("{}", hbtiPhoto.getPhotoUrl());
            });
        }
    }
}
