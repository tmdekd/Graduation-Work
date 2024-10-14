package com.book.gobook.service;

import com.book.gobook.config.auth.PrincipalDetail;
import com.book.gobook.model.Comment;
import com.book.gobook.model.Question;
import com.book.gobook.repository.CommentRepository;
import com.book.gobook.repository.QuestionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QnAService {
    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private CommentRepository commentRepository;

    // 질문을 저장하는 메서드
    @Transactional
    public Question saveQuestion(Question question) {
        return questionRepository.save(question);
    }

    // 특정 회원이 작성한 모든 질문을 조회하는 메서드
    public List<Question> findQuestionsByMemberId(String memberId) {
        return questionRepository.findByMember_Id(memberId);
    }

    public List<Question> findAllQuestions() {
        return questionRepository.findAll();
    }

    public Question findQuestionById(Long questionId) {
        Optional<Question> result = questionRepository.findById(questionId);
        return result.orElse(null);
    }


    public Question getQuestionById(Long id) {
        return questionRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 ID의 질문이 존재하지 않습니다. id=" + id));
    }

    public void deleteQuestionById(Long id) {
        Question question = questionRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 ID의 질문이    재하지 않습니다. id=" + id));
        questionRepository.delete(question);
    }

    public void updateQuestion(Long id, Question existingQuestion) {
        Question question = questionRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 ID의 질문이    재하지 않습니다. id=" + id));
        question.setTitle(existingQuestion.getTitle());
        question.setContent(existingQuestion.getContent());
        question.setMember(existingQuestion.getMember());
        question.setCreatedDate(existingQuestion.getCreatedDate());
        questionRepository.save(question);
    }



    //댓글
    public Question findById(Long id) {
        return questionRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 ID의 질문이    재하지 않습니다. id=" + id));
    }


    //댓글

    @Transactional
    public Comment saveComment(Comment comment) {
        if (comment.getQuestion() == null) {
            throw new IllegalArgumentException("Question cannot be null");
        }
        return commentRepository.save(comment);
    }

    public List<Comment> findCommentsByQuestionId(Long questionId) {
        return commentRepository.findByQuestionId(questionId);
    }


    public Comment findCommentById(Long id) {
        return commentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 ID의 댓글이    재하지 않습니다. id=" + id));
    }

    public void deleteCommentById(Long id) {
        commentRepository.deleteById(id);
    }
}