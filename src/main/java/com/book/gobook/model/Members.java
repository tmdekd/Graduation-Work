package com.book.gobook.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Table(name = "member")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@SequenceGenerator(
        name = "MEMBER_SEQ_GENERATOR"
        , sequenceName = "MEMBER_SEQ"
        , initialValue = 1
        , allocationSize = 1
        )
public class Members {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MEMBER_SEQ_GENERATOR")
    private int num;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 50, unique = true)
    private String phone;

    @Column(nullable = false, length = 50, unique = true)
    private String id;

    @Column(nullable = false, length = 70)
    private String pwd;

    @Column(nullable = false, length = 50)
    private String email;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date joindate;

    @Column(nullable = false, length = 70, unique = true)
    private String qrCode;

    @Column(nullable = false, length = 50)
    private String authority;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PurchaseHistory> histories = new ArrayList<>();
}
