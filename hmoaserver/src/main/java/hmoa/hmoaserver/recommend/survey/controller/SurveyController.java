package hmoa.hmoaserver.recommend.survey.controller;

import hmoa.hmoaserver.common.PageSize;
import hmoa.hmoaserver.common.ResultDto;
import hmoa.hmoaserver.community.domain.Category;
import hmoa.hmoaserver.community.domain.Community;
import hmoa.hmoaserver.community.dto.CommunityByHBTIResponseDto;
import hmoa.hmoaserver.community.service.CommunityService;
import hmoa.hmoaserver.hshop.domain.OrderEntity;
import hmoa.hmoaserver.hshop.service.OrderService;
import hmoa.hmoaserver.member.domain.Member;
import hmoa.hmoaserver.member.service.MemberService;
import hmoa.hmoaserver.note.domain.Note;
import hmoa.hmoaserver.note.domain.NoteSynonym;
import hmoa.hmoaserver.note.dto.NoteSimpleResponseDto;
import hmoa.hmoaserver.note.service.NoteCachingService;
import hmoa.hmoaserver.note.service.NoteService;
import hmoa.hmoaserver.note.service.NoteSynonymService;
import hmoa.hmoaserver.perfume.dto.PerfumeRecommendation;
import hmoa.hmoaserver.perfume.service.PerfumeService;
import hmoa.hmoaserver.recommend.survey.controller.constant.RecommendType;
import hmoa.hmoaserver.recommend.survey.controller.constant.SurveyConstant;
import hmoa.hmoaserver.recommend.survey.domain.*;
import hmoa.hmoaserver.recommend.survey.dto.*;
import hmoa.hmoaserver.recommend.survey.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
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

    @Value("${survey.image.background}")
    private String backgroundImgUrl;
    @Value("${survey.image.first}")
    private String firstImgUrl;
    @Value("${survey.image.second}")
    private String secondImgUrl;

    private final SurveyService surveyService;
    private final SurveyCachingService surveyCachingService;
    private final QuestionService questionService;
    private final NoteCachingService noteCachingService;
    private final AnswerService answerService;
    private final AnswerNoteService answerNoteService;
    private final NoteService noteService;
    private final MemberService memberService;
    private final MemberAnswerService memberAnswerService;
    private final NoteRecommendService noteRecommendService;
    private final PerfumeService perfumeService;
    private final NoteSynonymService noteSynonymService;
    private final OrderService orderService;
    private final CommunityService communityService;

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
        Question question = questionService.getQuestion(questionId);
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
        Survey survey = surveyCachingService.getSurvey(SurveyType.NOTE);
        SurveyResponseDto result = new SurveyResponseDto(survey);

        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "향bti 홈 이미지, 향수 추천 서비스 가능 여부")
    @GetMapping("/home")
    public ResponseEntity<SurveyHomeResponseDto> getHomeSurvey(@RequestHeader("X-AUTH-TOKEN") String token) {
        Member member = memberService.findByMember(token);
        List<OrderEntity> orders = orderService.findByMemberId(member.getId());

        return ResponseEntity.ok(new SurveyHomeResponseDto(backgroundImgUrl, firstImgUrl, secondImgUrl, !orders.isEmpty()));
    }

    @ApiOperation(value = "향수 추천 설문 조회")
    @GetMapping("/perfume")
    public ResponseEntity<PerfumeSurveyResponseDto> getPerfumeRecommendSurvey() {
        List<Note> notes = noteCachingService.getNotes();
        Question question = surveyCachingService.getQuestionById(SurveyConstant.QUESTION_ID);

        return ResponseEntity.ok(surveyCachingService.getPerfumeSurvey(notes, question));
    }

    @ApiOperation(value = "향수 추천 API")
    @PostMapping("/perfume/respond")
    public ResponseEntity<PerfumeRecommendsResponseDto> respondPerfumeRecommendSurvey(@RequestHeader("X-AUTH-TOKEN") String token, @RequestParam RecommendType recommendType, @RequestBody PerfumeRecommendRequestDto dto) {
        Member member = memberService.findByMember(token);

        List<PerfumeRecommendation> perfumeRecommendations;

        if (recommendType == RecommendType.NOTE) {
            perfumeRecommendations = perfumeService.recommendPerfumesIncludePrice(dto.getMinPrice(), dto.getMaxPrice(), getSearchNotes(dto.getNotes()));
        } else {
            perfumeRecommendations = perfumeService.recommendPerfumes(dto.getMinPrice(), dto.getMaxPrice(), getSearchNotes(dto.getNotes()));
        }

        List<PerfumeRecommendResponseDto> perfumeSimilarResponseDtos = perfumeRecommendations.stream().map(perfumeRecommendation -> new PerfumeRecommendResponseDto(perfumeRecommendation.getPerfume())).collect(Collectors.toList());

        return ResponseEntity.ok(new PerfumeRecommendsResponseDto(perfumeSimilarResponseDtos));
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

        //이미 추천 노트가 존재하면 지우기
        List<NoteRecommend> noteRecommends = noteRecommendService.findByMember(member);
        if (noteRecommends.size() > 0) {
            for (NoteRecommend noteRecommend : noteRecommends) {
                noteRecommendService.delete(noteRecommend);
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

    private List<String> getSearchNotes(List<String> notes) {
        List<String> searchNotes = new ArrayList<>();

        for (String note : notes) {
            searchNotes.add(note);
            NoteSynonym noteSynonym = noteSynonymService.getNoteSynonym(note);
            searchNotes.addAll(noteSynonym.getSynonyms());
        }

        return searchNotes;
    }
}
