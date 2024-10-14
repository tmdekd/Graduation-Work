package com.book.gobook.token;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Component
public class TokenGenerator {
    private Map<String, Instant> tokenExpiryMap = new HashMap<>();

    public String generateTokenWithExpiry() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[24];
        random.nextBytes(bytes);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);

        // 토큰의 유효 기간을 현재 시간으로부터 3분 후로 설정
        Instant expiryTime = Instant.now().plusSeconds(180); // 180초 == 3분
        tokenExpiryMap.put(token, expiryTime);

        return token;
    }

    // 토큰의 유효성 검사
    public boolean isTokenValid(String token) {
        Instant expiryTime = tokenExpiryMap.get(token);
        if (expiryTime == null) {
            return false; // 토큰이 없는 경우
        }
        return Instant.now().isBefore(expiryTime); // 현재 시간이 유효 기간 이전인지 확인
    }
}
