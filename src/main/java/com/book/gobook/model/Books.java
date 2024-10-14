package com.book.gobook.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Table(name = "book")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@SequenceGenerator(
        name = "BOOK_SEQ_GENERATOR"
        , sequenceName = "BOOK_SEQ"
        , initialValue = 1
        , allocationSize = 1
)
public class Books {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BOOK_SEQ_GENERATOR")
    private long num;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 50)
    private String author;

    @Column(nullable = false, length = 50)
    private String publisher;

    @Column(nullable = false, length = 50)
    private String category;

    @Column(nullable = false, length = 50)
    private int price;

    @Column(nullable = false, length = 150)
    private String introduction;

    @Column(nullable = false, length = 50, unique = true)
    private String uid;

    @Column(nullable = false, length = 50)
    private String img;

    @Column(nullable = false, length = 30)
    private String location;

    @Column(nullable = false, length = 50)
    private long cnt;

    @Column(nullable = false, length = 50)
    private long cnt_noCategory;

    @Column(nullable = false, length = 30)
    private long sales;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PurchaseHistory> histories = new ArrayList<>();

}
