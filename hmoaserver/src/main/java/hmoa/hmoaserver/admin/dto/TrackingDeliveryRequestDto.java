package hmoa.hmoaserver.admin.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class TrackingDeliveryRequestDto {
    private String query;
    private Object variables;

    public TrackingDeliveryRequestDto(String query, Object variables) {
        this.query = query;
        this.variables = variables;
    }
}
