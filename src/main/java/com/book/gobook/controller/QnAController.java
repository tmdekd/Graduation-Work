package com.book.gobook.controller;

import com.book.gobook.config.auth.PrincipalDetail;
import com.book.gobook.model.Comment;
import com.book.gobook.model.Members;
import com.book.gobook.model.Question;
import com.book.gobook.service.QnAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.List;

@Controller
public class QnAController {
    @Autowired
    private QnAService qnaService;

    @GetMapping("/auth/qna")
    public String showQnA(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 사용자 정보가 있는지 확인, 인증된 사용자인지 확인, 사용자 객체가 PrincipalDetail 타입인지 확인
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof PrincipalDetail) {
            PrincipalDetail principal = (PrincipalDetail) authentication.getPrincipal();
            model.addAttribute("userPrincipal", principal);

            Members members = principal.getId();
            String memberId = members.getId();

            // 작성글 목록 조회
            List<Question> questionList = qnaService.findQuestionsByMemberId(memberId);
            model.addAttribute("questionList", questionList);
        }

        List<Question> questionList = qnaService.findAllQuestions();
        model.addAttribute("questionList", questionList);


        return "board/qna";
    }

    // 질문 작성 폼 페이지로 이동
    @GetMapping("/qna_question")
    public String createQuestionForm(Model model) {
        // 질문 작성을 위한 빈 Question 객체를 모델에 추가
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof PrincipalDetail) {
            PrincipalDetail principal = (PrincipalDetail) authentication.getPrincipal();
            model.addAttribute("userPrincipal", principal);
        }
        model.addAttribute("question", new Question());
        return "board/qna_question";
    }

    @GetMapping("/auth/qna_detail/{id}")
    public String QnADetail(@PathVariable("id") Long questionId, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthor = false; // 기본값은 false
        boolean isUser = false; // 기본값은 false
        // 사용자 정보가 있는지 확인, 인증된 사용자인지 확인, 사용자 객체가 PrincipalDetail 타입인지 확인
        PrincipalDetail principal = null;
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof PrincipalDetail) {
            principal = (PrincipalDetail) authentication.getPrincipal();
            model.addAttribute("userPrincipal", principal);

            Members members = principal.getId();
            //String memberId = members.getId();
        }
        // 질문 상세 정보 조회
        Question question = qnaService.findQuestionById(questionId);
        if (question == null) {
            return "redirect:/error";
        }
        // 로그인한 사용자가 게시글 작성자인지 확인-> 관리자권한으로
        //if (principal != null && question.getMember().getId().equals(principal.getId().getId())) 로그인한 사용자 아이디가 관리자아이디인지 (admin)
//        if (principal != null && question.getMember().getId().equals(principal.getId().getId())) {
//            isAuthor = true;
//        }
        if (principal != null && "admin".equals(principal.getUsername())) {
            isAuthor = true;
        }
        else if (principal != null && question.getMember().getId().equals(principal.getId().getId())) {
            isUser = true;
        }
        model.addAttribute("question", question);
        model.addAttribute("isAuthor", isAuthor); // 수정, 삭제 버튼 표시 여부
        model.addAttribute("isUser", isUser);

        // 댓글 목록 조회
        List<Comment> comment = qnaService.findCommentsByQuestionId(questionId);
        model.addAttribute("comment", comment);
        model.addAttribute("questionId", questionId);

        // 로그인 여부와 무관하게 질문 상세 페이지 반환
        return "board/qna_detail";
    }

    // 수정 페이지로 이동하는 메서드
    @GetMapping("/qna/question/edit/{id}")
    public String editQuestionForm(@PathVariable Long id, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof PrincipalDetail) {
            PrincipalDetail principal = (PrincipalDetail) authentication.getPrincipal();
            model.addAttribute("userPrincipal", principal);
        }


        // 해당 질문 조회
        Question question = qnaService.findQuestionById(id);
        model.addAttribute("question", question);
        model.addAttribute("questionId", id);
        return "board/qna_edit"; // 수정 페이지로 이동
    }



}