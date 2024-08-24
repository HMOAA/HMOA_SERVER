package hmoa.hmoaserver.hshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class BootpayConfirmRequestDto {

    @NotNull(message = "receiptId가 없습니다")
    private String receiptId;
}
