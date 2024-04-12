package hmoa.hmoaserver.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class PagingDto<D> {
    private final D data;
    private boolean isLastPage = false;
}
