package hmoa.hmoaserver.magazine.dto;

import hmoa.hmoaserver.magazine.domain.ContentRequest;
import hmoa.hmoaserver.magazine.domain.Magazine;
import lombok.Data;

@Data
public class ContentRequestDto {
    private String type;
    private String data;

    public ContentRequest toEntity(Magazine magazine) {
        return ContentRequest.builder()
                .type(type)
                .data(data)
                .magazine(magazine)
                .build();
    }
}
