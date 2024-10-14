package com.book.gobook.config.auth;

import com.book.gobook.model.Members;
import com.book.gobook.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class PrincipalDetailService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Autowired
    public PrincipalDetailService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        System.out.println("id = " + id);
        try {
            Members principal = memberRepository.findById(id).orElseThrow(() -> {
                return new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다. : " + id);
            });
            return new PrincipalDetail(principal);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UsernameNotFoundException("로그인 중 오류가 발생했습니다.");
        }
    }
}
