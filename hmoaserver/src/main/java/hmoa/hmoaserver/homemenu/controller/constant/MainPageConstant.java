package hmoa.hmoaserver.homemenu.controller.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MainPageConstant {
    BRAND_HOME(1L),
    FIRST_HOME(2L),
    SECOND_HOME(3L),
    THIRD_HOME(4L),
    BANNER(5L);

    private final Long id;
}
