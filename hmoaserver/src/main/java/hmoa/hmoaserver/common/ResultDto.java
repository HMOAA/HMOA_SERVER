package hmoa.hmoaserver.common;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
public class ResultDto<D> {

    private final String resultCode;
    private final String message;
    private final D data;

    public ResultDto(final String resultCode, final String message) {
        this.resultCode = resultCode;
        this.message = message;
        this.data = null;
    }

    public static <D> ResultDto<D> response(final String resultCode, final String message) {
        return response(resultCode, message, null);
    }
    public static <D> ResultDto<D> response(final String resultCode,final String message,final D data){
        return ResultDto.<D>builder()
                .resultCode(resultCode)
                .message(message)
                .data(data)
                .build();
    }
}
