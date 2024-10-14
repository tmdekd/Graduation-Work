package com.book.gobook.model;


import jakarta.persistence.*;
import lombok.*;

@Table(name = "wishlist")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SequenceGenerator(
        name = "WISH_LIST_SEQ_GENERATOR"
        , sequenceName = "WISH_LIST_SEQ"
        , initialValue = 1
        , allocationSize = 1
)
@Entity
public class WishList {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "WISH_LIST_SEQ_GENERATOR")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_num", nullable = false)
    private Books book;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_num", nullable = false)
    private Members member;

    public void setMembers(Members member) {
        this.member = member;
    }

    public void setBooks(Books book) {
        this.book = book;
    }

}