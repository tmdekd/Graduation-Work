package com.book.gobook.model;

import jakarta.persistence.*;
import lombok.*;


@Table(name = "comment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SequenceGenerator(
        name = "COMMENT_SEQ_GENERATOR",
        sequenceName = "COMMENT_SEQ",
        initialValue = 1,
        allocationSize = 1
)
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COMMENT_SEQ_GENERATOR")
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_num", nullable = false)
    private Members member; // 'Members' 엔티티를 참조하여 댓글을 작성한 사용자를 연결

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question; // 'Question' 엔티티를 참조하여 댓글이 달린 질문을 연결

    public void setQuestion(Question question) {
        this.question = question;
    }



}
