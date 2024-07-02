package hmoa.hmoaserver.recommend.survey.controller;

import hmoa.hmoaserver.common.ResultDto;
import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.member.service.MemberService;
import hmoa.hmoaserver.note.domain.Note;
import hmoa.hmoaserver.note.dto.NoteSimpleResponseDto;
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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    private final NoteRecommendService noteRecommendService;

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
    public ResponseEntity<NoteRecommendResponseDto> respondNoteRecommendSurvey(@RequestHeader("X-AUTH-TOKEN") String token, @RequestBody MemberAnswerRequestDto dto) {
        Member member = memberService.findByMember(token);
        //이미 응답이 존재하면 지우기
        if (memberAnswerService.isExistingMemberAnswer(member)) {
            for (MemberAnswer memberAnswer : memberAnswerService.findByMember(member)) {
                Answer answer = answerService.findById(memberAnswer.getAnswer().getId());
                memberAnswerService.deleteMemberAnswer(member, answer);
            }
        }

        Answer answer;

        //optionId로 멤버가 응답한 답변 저장
        for (Long optionId : dto.getOptionIds()) {
            answer = answerService.findById(optionId);

            memberAnswerService.save(MemberAnswer.builder().member(member).answer(answer).build());
        }

        //추천 노트 저장
        List<String> notePoints = noteRecommendService.calculateNoteScoreFromMemberAnswer(member.getMemberAnswers());
        noteRecommendService.save(notePoints, member);

        Note note;
        List<Note> recommendNotes = new ArrayList<>();

        //추천 노트 3개 뽑기
        for (int i = 0; i < notePoints.size(); i++) {
            if (i == 3) {
                break;
            }
            note = noteService.findByTitle(notePoints.get(i));
            recommendNotes.add(note);
        }

        NoteRecommendResponseDto result = new NoteRecommendResponseDto(recommendNotes.stream().map(NoteSimpleResponseDto::new).collect(Collectors.toList()));

        return ResponseEntity.ok(result);
    }
}
