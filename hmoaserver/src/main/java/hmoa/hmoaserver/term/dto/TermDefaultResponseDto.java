package hmoa.hmoaserver.term.dto;

import hmoa.hmoaserver.term.domain.Term;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TermDefaultResponseDto {
    private Long termId;
    private String termTitle;
    private String termEnglishTitle;
    private String content;

    public TermDefaultResponseDto(Term term) {
        this.termId = term.getId();
        this.termTitle = term.getTitle();
        this.termEnglishTitle = term.getEnglishTitle();
        this.content = term.getContent();
    }
}
