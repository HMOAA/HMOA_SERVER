package hmoa.hmoaserver.perfumer.dto;

import hmoa.hmoaserver.perfumer.domain.Perfumer;
import lombok.Data;

@Data
public class PerfumerSaveRequestDto {
    private String perfumerTitle;
    private String perfumerSubTitle;
    private String content;

    public Perfumer toEntity() {
        return Perfumer.builder()
                .title(perfumerTitle)
                .subTitle(perfumerSubTitle)
                .content(content)
                .build();
    }
}
