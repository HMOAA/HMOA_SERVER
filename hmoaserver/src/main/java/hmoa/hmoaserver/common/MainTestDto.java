package hmoa.hmoaserver.common;

import hmoa.hmoaserver.perfume.domain.Perfume;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@ToString
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class MainTestDto {
    private Long id;
    private String brandName;
    private String perfumeName;
    private String imageUrl;

    public MainTestDto(Perfume perfume) {
        this.id=perfume.getId();
        this.brandName=perfume.getBrand().getBrandName();
        this.perfumeName=perfume.getKoreanName();
        this.imageUrl=perfume.getPerfumePhoto().getPhotoUrl();
    }

}
