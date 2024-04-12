package hmoa.hmoaserver.common;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
public class ResultDto<D> {

    private final D data;
}
