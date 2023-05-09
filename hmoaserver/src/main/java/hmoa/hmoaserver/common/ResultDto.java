package hmoa.hmoaserver.common;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
public class ResultDto<D> {

    private final D data;

//    public ResultDto(final String resultCode, final String message) {
//        this.data = null;
//    }
//
////    public static <D> ResultDto<D> response(final String resultCode, final String message) {
////        return response(resultCode, message, null);
////    }
//    public static <D> ResultDto<D> response(final String resultCode,final String message,final D data){
//        return ResultDto.<D>builder()
//                .data(data)
//                .build();
//    }
}
