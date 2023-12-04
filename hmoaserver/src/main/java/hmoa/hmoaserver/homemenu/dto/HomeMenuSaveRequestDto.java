package hmoa.hmoaserver.homemenu.dto;

import hmoa.hmoaserver.homemenu.domain.HomeMenu;
import lombok.Data;

@Data
public class HomeMenuSaveRequestDto {
    private String title;

    public HomeMenu toEntity(){
        return HomeMenu.builder()
                .title(title)
                .build();
    }
}
