package hmoa.hmoaserver.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class TestDto<T> {
    private String title;
    private T perfumeList;
//    private String title2;
//
//    private T perfumes2;
//    private String title3;
//
//    private T perfumes3;

}
