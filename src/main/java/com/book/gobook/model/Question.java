package com.book.gobook.model;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "question")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SequenceGenerator(
        name = "QUESTION_SEQ_GENERATOR",
        sequenceName = "QUESTION_SEQ",
        initialValue = 1,
        allocationSize = 1
)

@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QUESTION_SEQ_GENERATOR")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_num")
    private Members member; // 'Members' 엔티티를 참조하여 질문을 작성한 사용자를 연결


    public void setMember(Members member) {
        this.member = member;
    }
    // 추가된 getMemberId 메서드


}