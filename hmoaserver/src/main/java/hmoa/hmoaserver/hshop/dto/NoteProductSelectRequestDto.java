package hmoa.hmoaserver.hshop.dto;

import lombok.Data;

import java.util.List;

@Data
public class NoteProductSelectRequestDto {

    private List<Long> productIds;
}
