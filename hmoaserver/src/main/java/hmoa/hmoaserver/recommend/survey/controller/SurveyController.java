package hmoa.hmoaserver.recommend.survey.controller;

import hmoa.hmoaserver.common.ResultDto;
import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.member.service.MemberService;
import hmoa.hmoaserver.note.domain.Note;
import hmoa.hmoaserver.note.service.NoteService;
import hmoa.hmoaserver.recommend.survey.domain.*;
import hmoa.hmoaserver.recommend.survey.dto.*;
import hmoa.hmoaserver.recommend.survey.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = {"설문"})
@RestController
@RequestMapping("/survey")
@Slf4j
@RequiredArgsConstructor
public class SurveyController {

    private final SurveyService surveyService;
    private final QuestionService questionService;
    private final AnswerService answerService;
    private final AnswerNoteService answerNoteService;
    private final NoteService noteService;
    private final MemberService memberService;
    private final MemberAnswerService memberAnswerService;

    @PostMapping("/save")
    public ResponseEntity<ResultDto<Object>> saveSurvey(@RequestBody SurveySaveRequestDto dto) {
        surveyService.save(dto);

        return ResponseEntity.ok(ResultDto.builder().build());
    }

    @PostMapping("/save-question/{surveyId}")
    public ResponseEntity<ResultDto<Object>> saveQuestion(@PathVariable Long surveyId, @RequestBody QuestionSaveRequestDto dto) {
        Survey survey = surveyService.findById(surveyId);
        questionService.save(dto, survey);

        return ResponseEntity.ok(ResultDto.builder().build());
    }

    @PostMapping("/save-answer/{questionId}")
    public ResponseEntity<ResultDto<Object>> saveAnswer(@PathVariable Long questionId, @RequestBody AnswerSaveRequestDto dto) {
        Question question = questionService.findById(questionId);
        answerService.save(dto, question);

        return ResponseEntity.ok(ResultDto.builder().build());
    }

    @PostMapping("/save-answer-note")
    public ResponseEntity<ResultDto<Object>> saveAnswerNote(@RequestBody List<AnswerNoteSaveRequestDto> dtos) {
        Note note;
        Answer answer;

        for (AnswerNoteSaveRequestDto dto : dtos) {
            note = noteService.findByTitle(dto.getNoteTitle());
            answer = answerService.findById(dto.getAnswerId());

            answerNoteService.save(AnswerNote.builder().note(note).answer(answer).build());
        }

        return ResponseEntity.ok(ResultDto.builder().build());
    }

    @ApiOperation(value = "향료 추천 설문 조회")
    @GetMapping("/note")
    public ResponseEntity<SurveyResponseDto> getNoteRecommendSurvey() {
        Survey survey = surveyService.findBySurveyType(SurveyType.NOTE);
        SurveyResponseDto result = new SurveyResponseDto(survey);

        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "향료 추천 응답 API")
    @PostMapping("/note/respond")
    public ResponseEntity<ResultDto<Object>> respondNoteRecommendSurvey(@RequestHeader("X-AUTH-TOKEN") String token, @RequestBody MemberAnswerRequestDto dto) {
        Member member = memberService.findByMember(token);
        if (memberAnswerService.isExistingMemberAnswer(member)) {
            for (MemberAnswer memberAnswer : memberAnswerService.findByMember(member)) {
                Answer answer = answerService.findById(memberAnswer.getAnswer().getId());
                memberAnswerService.deleteMemberAnswer(member, answer);
            }
        }

        Answer answer;

        for (Long optionId : dto.getOptionIds()) {
            answer = answerService.findById(optionId);

            memberAnswerService.save(MemberAnswer.builder().member(member).answer(answer).build());
        }
        return ResponseEntity.ok(ResultDto.builder().build());
    }
}
