package hmoa.hmoaserver.perfume.dto;

import hmoa.hmoaserver.perfume.domain.Perfume;
import lombok.Getter;

@Getter
public class PerfumeRecommendation {
    private Perfume perfume;
    private int score;

    public PerfumeRecommendation(Perfume perfume, int score) {
        this.perfume = perfume;
        this.score = score;
    }
}
