package hmoa.hmoaserver.term.dto;

import hmoa.hmoaserver.term.domain.Term;
import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class TermSaveRequestDto {
    private String termTitle;
    private String termEnglishTitle;
    private String content;

    public Term toEntity() {
        return Term.builder()
                .title(termTitle)
                .englishTitle(termEnglishTitle)
                .content(content)
                .build();
    }
}
