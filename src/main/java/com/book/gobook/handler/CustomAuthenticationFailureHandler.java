package com.book.gobook.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        request.getSession().setAttribute("errorMessage", "로그인에 실패하였습니다. 아이디와 비밀번호를 확인해주세요.");
        response.sendRedirect("/auth/login_joinForm");
    }

}
