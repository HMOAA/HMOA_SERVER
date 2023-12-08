package hmoa.hmoaserver.homemenu.dto;

import hmoa.hmoaserver.common.DefaultValues;
import lombok.Data;

@Data
public class HomeMenuFirstResponseDto {
    private String mainImage = DefaultValues.getMainImgUrl();
    private HomeMenuDefaultResponseDto firstMenu;

    public HomeMenuFirstResponseDto(HomeMenuDefaultResponseDto firstMenu) {
        this.firstMenu = firstMenu;
    }
}
