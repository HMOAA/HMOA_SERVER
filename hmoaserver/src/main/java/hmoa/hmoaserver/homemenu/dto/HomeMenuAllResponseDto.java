package hmoa.hmoaserver.homemenu.dto;

import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.perfume.domain.Perfume;
import lombok.Data;

@Data
public class HomeMenuAllResponseDto {
    private Long perfumeId;
    private String brandName;
    private String perfumeName;
    private String imgUrl;
    private boolean isLiked = false;

    public HomeMenuAllResponseDto(Perfume perfume) {
        this.perfumeId = perfume.getId();
        this.brandName = perfume.getBrand().getBrandName();
        this.perfumeName = perfume.getKoreanName();
        this.imgUrl = perfume.getPerfumePhoto().getPhotoUrl();
    }

    public HomeMenuAllResponseDto(Perfume perfume, boolean isLiked) {
        this.perfumeId = perfume.getId();
        this.brandName = perfume.getBrand().getBrandName();
        this.perfumeName = perfume.getKoreanName();
        this.imgUrl = perfume.getPerfumePhoto().getPhotoUrl();
        this.isLiked = isLiked;
    }
}
