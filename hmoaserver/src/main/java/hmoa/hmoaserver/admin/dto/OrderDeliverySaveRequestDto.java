package hmoa.hmoaserver.admin.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderDeliverySaveRequestDto {

    private Long orderId;
    private String courierCountry;
    private String trackingNumber;
}
