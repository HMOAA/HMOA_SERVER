package hmoa.hmoaserver.recommend.survey.dto;

import hmoa.hmoaserver.note.dto.NoteSimpleResponseDto;
import lombok.Data;

import java.util.List;

@Data
public class NoteRecommendResponseDto {

    private List<NoteSimpleResponseDto> recommendNotes;

    public NoteRecommendResponseDto(List<NoteSimpleResponseDto> recommendNotes) {
        this.recommendNotes = recommendNotes;
    }
}
