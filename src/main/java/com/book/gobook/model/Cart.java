package com.book.gobook.model;

import jakarta.persistence.*;
import lombok.*;

import java.awt.print.Book;

@Table(name = "cart")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@SequenceGenerator(
        name = "CART_SEQ_GENERATOR",
        sequenceName = "CART_SEQ",
        initialValue = 1,
        allocationSize = 1
)
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CART_SEQ_GENERATOR")
    private long id;

    // Books 엔티티와의 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_num")
    private Books book;

    // Members 엔티티와의 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_num")
    private Members member;

    @Column(nullable = false)
    private int quantity; // 장바구니에 담긴 해당 책의 수량

}
