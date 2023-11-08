package hmoa.hmoaserver.perfume.dto;

import hmoa.hmoaserver.perfume.domain.Perfume;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PerfumeByBrandResponseDto {
    private Long brandId;
    private String brandName;
    private String brandEnglishName;
    private String brandImgUrl;

    private Long perfumeId;
    private String perfumeImgUrl;
    private String perfumeName;
    private int heartCount;
    private boolean isLiked = false;

    public PerfumeByBrandResponseDto(Perfume perfume, boolean isLiked){
        this.brandName=perfume.getBrand().getBrandName();
        this.brandId=perfume.getBrand().getId();
        this.brandEnglishName=perfume.getBrand().getEnglishName();
        this.brandImgUrl=perfume.getBrand().getBrandPhoto().getPhotoUrl();
        this.perfumeId = perfume.getId();
        this.perfumeName=perfume.getKoreanName();
        this.perfumeImgUrl=perfume.getPerfumePhoto().getPhotoUrl();
        this.isLiked = isLiked;
        this.heartCount = perfume.getHeartCount();
    }
}
