package hmoa.hmoaserver.search.dto;

import hmoa.hmoaserver.brand.domain.Brand;
import hmoa.hmoaserver.brand.dto.BrandDefaultResponseDto;
import hmoa.hmoaserver.search.service.UnicodeService;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class SearchBrandResponseDto {
    @ApiModelProperty(position = 0)
    private int consonant;
    @ApiModelProperty(position = 1)
    private List<BrandDefaultResponseDto> brandList;
}
