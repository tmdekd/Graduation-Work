package com.book.gobook.config;

import com.book.gobook.config.auth.PrincipalDetailService;
import com.book.gobook.handler.CustomAuthenticationFailureHandler;
import com.book.gobook.handler.CustomAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final PrincipalDetailService principalDetailService;
    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Autowired
    public SecurityConfig(PrincipalDetailService principalDetailService, CustomAuthenticationFailureHandler customAuthenticationFailureHandler, CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler) {
        this.principalDetailService = principalDetailService;
        this.customAuthenticationFailureHandler = customAuthenticationFailureHandler;
        this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
    }

    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        auth.userDetailsService(principalDetailService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/", "/auth/**", "/js/**", "/css/**", "/img/**").permitAll()
                                .requestMatchers("https://fonts.gstatic.com").permitAll()
                                .requestMatchers("https://kit.fontawesome.com/8978c32416.js").permitAll()
                                .requestMatchers("https://fonts.googleapis.com").permitAll()
                                .requestMatchers("https://code.jquery.com/**.js").permitAll()
                                .requestMatchers("https://cdn.jsdelivr.net/npm/sweetalert2@11").permitAll()
                                .requestMatchers("https://fonts.googleapis.com/css2?family=Nanum+Gothic&display=swap").permitAll()
                                .requestMatchers(HttpMethod.POST, "/auth/joinProc").permitAll()
                                .requestMatchers(HttpMethod.POST, "/auth/send_reset_email").permitAll()
                                .requestMatchers(HttpMethod.POST, "/auth/resetPwd").permitAll()
                                .requestMatchers(HttpMethod.POST, "/auth/findPurchaseHistory").permitAll()
                                .anyRequest().authenticated()
                )
                .formLogin(formLogin ->
                        formLogin
                                .loginPage("/auth/login_joinForm")
                                .loginProcessingUrl("/auth/loginProc")
                                .successHandler(customAuthenticationSuccessHandler)
                                .failureHandler(customAuthenticationFailureHandler)
                                .usernameParameter("id")
                                .passwordParameter("pwd")
                                .defaultSuccessUrl("/", true).permitAll()
                )
                .logout(logout ->
                        logout
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/")
                );

        return http.build();
    }
}
