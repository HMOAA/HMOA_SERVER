package hmoa.hmoaserver.perfume.dto;

import lombok.Data;

@Data
public class PerfumeSimilarResponseDto {
    private String brandName;
    private String perfumeImgUrl;
    private String perfumeName;
    public PerfumeSimilarResponseDto(String brandName,String perfumeImgUrl,String perfumeName){
        this.brandName=brandName;
        this.perfumeName=perfumeName;
        this.perfumeImgUrl=perfumeImgUrl;
    }
}
