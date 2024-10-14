package com.book.gobook.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "reset_password_tokens")
@SequenceGenerator(
        name = "TOKEN_SEQ_GENERATOR"
        , sequenceName = "TOKEN_SEQ"
        , initialValue = 1
        , allocationSize = 1
)
public class ResetPasswordToken {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TOKEN_SEQ_GENERATOR")
    private int tokenId;

    @Column(nullable = false)
    private int userId;

    @Column(nullable = false, length = 255)
    private String token;

    @Column(nullable = false)
    private LocalDateTime creationDate;

    @Column(nullable = false)
    private LocalDateTime expirationDate;

}
