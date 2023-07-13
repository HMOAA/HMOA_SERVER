package hmoa.hmoaserver.search.dto;

import hmoa.hmoaserver.perfume.domain.Perfume;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class PerfumeNameSearchResponseDto {
    private String perfumeName;
    public PerfumeNameSearchResponseDto(Perfume perfume){
        this.perfumeName=perfume.getKoreanName();
    }
}
