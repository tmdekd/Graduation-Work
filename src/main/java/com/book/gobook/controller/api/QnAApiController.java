package com.book.gobook.controller.api;

import com.book.gobook.config.auth.PrincipalDetail;
import com.book.gobook.dto.ResponseDto;
import com.book.gobook.model.Comment;
import com.book.gobook.model.Question;
import com.book.gobook.service.QnAService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@RestController
@RequestMapping("/qna")
public class QnAApiController {
    @Autowired
    private QnAService qnaService;

    // 문의사항 글 작성 기능
    @PostMapping("/qna_question")
    public <LocalDateTime> ResponseDto<Integer> createQuestion(@RequestBody Question question) {
        log.info("QnAApiController: createQuestion 호출됨");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication.getPrincipal() instanceof PrincipalDetail)) {
            return new ResponseDto<>(HttpStatus.FORBIDDEN.value(), -1); // 권한 없음
        }

        PrincipalDetail principal = (PrincipalDetail) authentication.getPrincipal();
        question.setMember(principal.getMembers());

        // 한국 시간대를 기준으로 현재 날짜와 시간을 설정
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.of("Asia/Seoul"));
        String nowInKorea = formatter.format(ZonedDateTime.now(ZoneId.of("Asia/Seoul")));
        question.setCreatedDate(nowInKorea);


        qnaService.saveQuestion(question);
        return new ResponseDto<>(HttpStatus.OK.value(), 1); // 성공적으로 추가됨
    }

    @PutMapping("/question/edit/{id}")
    public ResponseDto<Integer> editQuestion(@PathVariable Long id, @RequestBody Question question) {
        log.info("QnAApiController: editQuestion 호출됨");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication.getPrincipal() instanceof PrincipalDetail)) {
            return new ResponseDto<>(HttpStatus.FORBIDDEN.value(), -1); // 권한 없음
        }

        PrincipalDetail principal = (PrincipalDetail) authentication.getPrincipal();
        // 기존의 question 객체를 가져오거나, 새로운 객체를 생성하여 초기화
        Question existingQuestion = qnaService.findById(id);
        if (existingQuestion == null) {
            return new ResponseDto<>(HttpStatus.NOT_FOUND.value(), -1); // 해당 ID에 해당하는 question이 없음
        }

        // 로그인한 사용자와 게시글 작성자를 비교하여 권한 확인
        if (!principal.getUsername().equals(existingQuestion.getMember().getId())) {
            return new ResponseDto<>(HttpStatus.FORBIDDEN.value(), -1); // 작성자가 아닌 경우 권한 없음
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.of("Asia/Seoul"));
        String nowInKorea = formatter.format(ZonedDateTime.now(ZoneId.of("Asia/Seoul")));
        question.setCreatedDate(nowInKorea);

        // 질문 제목과 내용을 업데이트
        existingQuestion.setTitle(question.getTitle());
        existingQuestion.setContent(question.getContent());
        // 작성자 정보 업데이트
        existingQuestion.setMember(principal.getMembers());
        existingQuestion.setCreatedDate(question.getCreatedDate());

        qnaService.updateQuestion(id, existingQuestion);
        return new ResponseDto<>(HttpStatus.OK.value(), 1); // 성공적으로 수정됨
    }


    // 문의사항 글 삭제 기능
    @DeleteMapping("/question/delete/{id}")
    public ResponseDto<Integer> deleteQuestion(@PathVariable Long id) {
        log.info("QnAApiController: deleteQuestion 호출됨");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication.getPrincipal() instanceof PrincipalDetail)) {
            return new ResponseDto<>(HttpStatus.FORBIDDEN.value(), -1); // 권한 없음
        }

        PrincipalDetail principal = (PrincipalDetail) authentication.getPrincipal();
        Question question = qnaService.findQuestionById(id);
        if (question == null || !question.getMember().getId().equals(principal.getMembers().getId())) {
            return new ResponseDto<>(HttpStatus.FORBIDDEN.value(), -1); // 권한 없음
        }

        qnaService.deleteQuestionById(id);
        return new ResponseDto<>(HttpStatus.OK.value(), 1); // 성공적으로 삭제됨
    }

    //댓글작성
    @PostMapping("/qna_comment")
    public <LocalDateTime> ResponseDto<Integer> createComment(@RequestBody Comment comment) {
        log.info("QnAApiController: createComment 호출됨");
        System.out.println("comment.getContent() = " + comment.getContent());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication.getPrincipal() instanceof PrincipalDetail)) {
            return new ResponseDto<>(HttpStatus.FORBIDDEN.value(), -1); // 권한 없음
        }

        PrincipalDetail principal = (PrincipalDetail) authentication.getPrincipal();
        comment.setMember(principal.getMembers());

        Long questionId = comment.getQuestion().getId(); // 가정: 댓글에는 Question 객체가 있음
        Question question = qnaService.getQuestionById(questionId);
        comment.setQuestion(question);

        // 한국 시간대를 기준으로 현재 날짜와 시간을 설정
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.of("Asia/Seoul"));
        String nowInKorea = formatter.format(ZonedDateTime.now(ZoneId.of("Asia/Seoul")));
        comment.setCreatedDate(nowInKorea);



        qnaService.saveComment(comment);
        return new ResponseDto<>(HttpStatus.OK.value(), 1); // 성공적으로 추가됨
    }

    // 댓글 삭제 기능
    @DeleteMapping("/comment/delete/{id}")
    public ResponseDto<Integer> deleteComment(@PathVariable Long id) {
        log.info("QnAApiController: deleteComment호출됨, 댓글 id: {}", id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication.getPrincipal() instanceof PrincipalDetail)) {
            return new ResponseDto<>(HttpStatus.FORBIDDEN.value(), -1); // 권한 없음
        }

        PrincipalDetail principal = (PrincipalDetail) authentication.getPrincipal();


        Comment comment = qnaService.findCommentById(id);
        if (comment == null || !comment.getMember().getId().equals(principal.getMembers().getId())) {
            return new ResponseDto<>(HttpStatus.FORBIDDEN.value(), -1); // 권한 없음
        }


        qnaService.deleteCommentById(id);
        return new ResponseDto<>(HttpStatus.OK.value(), 1); // 성공적으로 삭제됨
    }

}