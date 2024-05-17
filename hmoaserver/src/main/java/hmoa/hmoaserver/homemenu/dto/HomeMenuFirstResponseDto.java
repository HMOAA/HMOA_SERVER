package hmoa.hmoaserver.homemenu.dto;

import hmoa.hmoaserver.common.DefaultValues;
import lombok.Data;

@Data
public class HomeMenuFirstResponseDto {
    private String mainImage = DefaultValues.getMainImgUrl();
    private String banner;
    private HomeMenuDefaultResponseDto firstMenu;

    public HomeMenuFirstResponseDto(HomeMenuDefaultResponseDto firstMenu, String banner) {
        this.firstMenu = firstMenu;
        this.banner = banner;
    }
}
