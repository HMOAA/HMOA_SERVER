package hmoa.hmoaserver.search.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SearchPerfumeResponseDto<T> {
    private final T perfumeList;

}
