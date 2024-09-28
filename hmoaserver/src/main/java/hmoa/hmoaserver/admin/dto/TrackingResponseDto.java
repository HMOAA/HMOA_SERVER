package hmoa.hmoaserver.admin.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TrackingResponseDto {

    private Data data;

    @Getter
    @NoArgsConstructor
    public static class Data {
        private Track track;
    }

    @Getter
    @NoArgsConstructor
    public static class Track {
        private LastEvent lastEvent;
    }

    @Getter
    @NoArgsConstructor
    public static class LastEvent {
        private String time;
        private Status status;
    }

    @Getter
    @NoArgsConstructor
    public static class Status {
        private String code;
    }
}