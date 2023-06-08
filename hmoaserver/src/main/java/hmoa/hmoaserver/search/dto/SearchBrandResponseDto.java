package hmoa.hmoaserver.search.dto;

import hmoa.hmoaserver.brand.domain.Brand;
import hmoa.hmoaserver.search.service.UnicodeService;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
public class SearchBrandResponseDto<T> {
    private int consonant;
    private final T brandList;
}
