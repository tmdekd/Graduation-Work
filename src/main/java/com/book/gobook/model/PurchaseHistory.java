package com.book.gobook.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.sql.Date;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "purchase_history")
@SequenceGenerator(
        name = "PURCHASE_HISTORY_SEQ_GENERATOR"
        , sequenceName = "PURCHASE_HISTORY_SEQ"
        , initialValue = 1
        , allocationSize = 1
)
public class PurchaseHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PURCHASE_HISTORY_SEQ_GENERATOR")
    private int id;

    // Books 엔티티와의 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_num")
    private Books book;

    @Column(nullable = false, length = 30)
    private String purchaseDate;

    @Column(nullable = false, length = 30)
    private String purchaseTime;

    @Column(nullable = false, length = 20)
    private String authenticationStatus;

    // Members 엔티티와의 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_num")
    private Members member;

    @Column(nullable = false, length = 30)
    private int quantity;

    @Column(nullable = false, length = 30)
    private int totalPrice;

}
