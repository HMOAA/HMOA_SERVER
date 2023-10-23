package hmoa.hmoaserver.admin.dto;

import hmoa.hmoaserver.admin.domain.HomeMenu;
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
