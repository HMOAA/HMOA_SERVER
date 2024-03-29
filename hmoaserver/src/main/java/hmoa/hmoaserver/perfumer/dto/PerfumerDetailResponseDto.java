package hmoa.hmoaserver.perfumer.dto;

import hmoa.hmoaserver.perfumer.domain.Perfumer;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PerfumerDetailResponseDto {
    private Long perfumerId;
    private String perfumerTitle;
    private String perfumerSubTitle;
    private String content;

    public PerfumerDetailResponseDto(Perfumer perfumer) {
        this.perfumerId = perfumer.getId();
        this.perfumerTitle = perfumer.getTitle();
        this.perfumerSubTitle = perfumer.getSubTitle();
        this.content = perfumer.getContent();
    }
}
