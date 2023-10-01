package hmoa.hmoaserver.admin.dto;

import hmoa.hmoaserver.admin.domain.PerfumeByHome;
import lombok.Data;

@Data
public class PerfumeByHomeSaveRequestDto {
    private String title;

    public PerfumeByHome toEntity(){
        return PerfumeByHome.builder()
                .title(title)
                .build();
    }
}
