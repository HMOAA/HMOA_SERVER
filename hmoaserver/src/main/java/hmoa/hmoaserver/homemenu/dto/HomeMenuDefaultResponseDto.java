package hmoa.hmoaserver.homemenu.dto;

import hmoa.hmoaserver.homemenu.domain.HomeMenu;
import lombok.Data;

import java.util.List;

@Data
public class HomeMenuDefaultResponseDto {
    private String title;
    private List<HomeMenuPerfumeResponseDto> perfumeList;

    public HomeMenuDefaultResponseDto(String title, List<HomeMenuPerfumeResponseDto> perfumeList) {
        this.title = title;
        this.perfumeList = perfumeList;
    }
}
