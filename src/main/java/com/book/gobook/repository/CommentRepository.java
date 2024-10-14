package com.book.gobook.repository;

import com.book.gobook.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    // 특정 사용자가 작성한 댓글을 찾는 메서드

    // 특정 질문에 달린 댓글을 찾는 메서드
    List<Comment> findByQuestionId(Long questionId);
}