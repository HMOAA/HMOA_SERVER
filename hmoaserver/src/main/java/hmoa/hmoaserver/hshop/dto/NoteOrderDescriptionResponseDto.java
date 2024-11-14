package hmoa.hmoaserver.hshop.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NoteOrderDescriptionResponseDto {

    private static final String TITLE1 = "향료 선택";
    private static final String TITLE2 = "배송";
    private static final String TITLE3 = "향수 추천";
    private static final String DESCRIPTION1 = "향BTI 검사 후 추천 받은 향료 카테고리 + 그 외에 선호 향료 카테고리 선택 가능 (카테고리 별 향료 3~5개)";
    private static final String DESCRIPTION2 = "결제 후 1~2일 내 향료 시향카드 배송 완료 (시향 후 책갈피로 활용 가능)";
    private static final String DESCRIPTION3 = "시향 후 가장 좋았던 향료 선택 + 향수 추천 받기";

    private List<NoteOrderDetailInfoDto> orderDescriptions;
    private String orderDescriptionImgUrl;

    public NoteOrderDescriptionResponseDto(String imgUrl) {
        this.orderDescriptionImgUrl = imgUrl;
        this.orderDescriptions = List.of(
                new NoteOrderDetailInfoDto(TITLE1, DESCRIPTION1),
                new NoteOrderDetailInfoDto(TITLE2, DESCRIPTION2),
                new NoteOrderDetailInfoDto(TITLE3, DESCRIPTION3)
        );
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class NoteOrderDetailInfoDto {
        private String title;
        private String content;

        public NoteOrderDetailInfoDto(String title, String content) {
            this.title = title;
            this.content = content;
        }
    }
}
