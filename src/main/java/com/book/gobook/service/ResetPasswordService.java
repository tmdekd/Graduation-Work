package com.book.gobook.service;

import com.book.gobook.model.ResetPasswordToken;
import com.book.gobook.repository.ResetPasswordTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ResetPasswordService {

    @Autowired
    private ResetPasswordTokenRepository tokenRepository;

    public void createResetPasswordToken(int userId, String token) {
        LocalDateTime now = LocalDateTime.now();
        ResetPasswordToken resetPasswordToken = ResetPasswordToken.builder()
                .userId(userId)
                .token(token)
                .creationDate(now)
                .expirationDate(now.plusMinutes(3))
                .build();

        tokenRepository.save(resetPasswordToken);
    }

    public ResetPasswordToken validateResetPasswordToken(String token) {
        // 토큰의 존재 여부 확인
        ResetPasswordToken resetPasswordToken = tokenRepository.findByToken(token);
        if (resetPasswordToken == null) {
            // 토큰이 데이터베이스에 존재하지 않음
            throw new IllegalArgumentException("Invalid token.");
        }

        // 토큰의 만료 여부 확인
        if (resetPasswordToken.getExpirationDate().isBefore(LocalDateTime.now())) {
            // 토큰의 유효 기간이 만료됨
            throw new IllegalArgumentException("Token has expired.");
        }

        // 검사를 모두 통과한 경우, 유효한 토큰 반환
        return resetPasswordToken;
    }
}
