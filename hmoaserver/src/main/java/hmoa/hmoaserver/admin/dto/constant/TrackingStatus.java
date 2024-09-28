package hmoa.hmoaserver.admin.dto.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TrackingStatus {
    DELIVERED("DELIVERED"),
    AVAILABLE_FOR_PICKUP("AVAILABLE_FOR_PICKUP");

    private final String value;
}
