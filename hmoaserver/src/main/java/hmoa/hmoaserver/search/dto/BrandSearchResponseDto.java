package hmoa.hmoaserver.search.dto;

import hmoa.hmoaserver.brand.dto.BrandDefaultResponseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class BrandSearchResponseDto {
    @ApiModelProperty(position = 0)
    private int consonant;
    @ApiModelProperty(position = 1)
    private List<BrandDefaultResponseDto> brandList;
}
