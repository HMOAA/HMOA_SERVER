package hmoa.hmoaserver.recommend.survey.dto;

import lombok.Data;

import java.util.List;

//응답한 옵션 목록 받기
@Data
public class MemberAnswerRequestDto {

    private List<Long> optionIds;
}
