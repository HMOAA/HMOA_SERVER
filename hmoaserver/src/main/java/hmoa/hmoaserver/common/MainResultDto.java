package hmoa.hmoaserver.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class MainResultDto<T> {
    private String mainImage;
    private final T recommend;

}
