package hmoa.hmoaserver.perfume.dto;

import hmoa.hmoaserver.perfume.domain.Perfume;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RecentPerfumeResponseDto {
    private Long perfumeId;
    private String brandName;
    private String perfumeName;
    private LocalDate relaseDate;
    private String perfumeImgUrl;

    public RecentPerfumeResponseDto(Perfume perfume) {
        this.perfumeId = perfume.getId();
        this.brandName = perfume.getBrand().getBrandName();
        this.perfumeName = perfume.getKoreanName();
        this.relaseDate = perfume.getReleaseDate();
        this.perfumeImgUrl = perfume.getPerfumePhoto().getPhotoUrl();
    }
}
