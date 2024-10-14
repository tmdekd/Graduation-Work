package com.book.gobook.service;

import com.book.gobook.model.Members;
import com.book.gobook.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

@Slf4j
@Service
public class MemberService {
    @Autowired
    private MemberRepository memberRepository;

    @Transactional
    public void join(Members members) {
        System.out.println("join 메서드 동작");
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        members.setJoindate(Date.valueOf(LocalDate.now()));
        
        // 비밀번호 암호화
        String rawPwd = members.getPwd();
        String encPwd = encoder.encode(rawPwd);
        members.setPwd(encPwd);
        
        // num 값을 이용하여 qrCode 생성
        String qrCodeValue = members.getId();
        members.setQrCode(qrCodeValue);
        System.out.println("rawNum = " + qrCodeValue);
        System.out.println("members.getQrCode() = " + members.getQrCode());

        members.setAuthority("USER");
        memberRepository.save(members);
    }

    @Transactional(readOnly = true)
    public boolean isUserIdAvailable(String id) {
        Optional<Members> existingMemberOptional = memberRepository.findById(id);
        return existingMemberOptional.isEmpty();
    }

    @Transactional(readOnly = true)
    public Members findMemberByQrCode(String qrCodeValue) {
        System.out.println("service의 findMemberByQrCode() 메서드 실행");
        System.out.println("qrCodeValue = " + qrCodeValue);
        // QR 코드 값으로 직접 회원을 조회
        Optional<Members> memberOptional = memberRepository.findByQrCode(qrCodeValue);

        if (memberOptional.isPresent()) {
            Members member = memberOptional.get();
            System.out.println("\n일치하는 회원정보가 존재합니다.");
            System.out.println("member = " + member + "\n");
            return member;
        } else {
            System.out.println("\n일치하는 회원정보가 존재하지 않습니다.");
            return null;
        }
    }


    @Transactional(readOnly = true)
    public Members findMemberByIdAndEmail(String id, String email) {
        return memberRepository.findByIdAndEmail(id, email);
    }

    @Transactional(readOnly = true)
    public Members findById(String memberId) {
        Optional<Members> member = memberRepository.findById(memberId);
        return member.orElse(null);
    }

    @Transactional
    public void save(Members members) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // 비밀번호 암호화
        String rawPwd = members.getPwd();
        String encPwd = encoder.encode(rawPwd);
        members.setPwd(encPwd);
        memberRepository.save(members);
    }

    public Members findMemberById(String principalId) {
        return memberRepository.findById(principalId).orElse(null);
    }

    @Transactional
    public void updateMember(Members member) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Members existingMember = memberRepository.findById(member.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다. id=" + member.getId()));

        String rawPwd = member.getPwd();
        if(rawPwd == null || rawPwd == "") {
            existingMember.setPwd(existingMember.getPwd());
            existingMember.setName(member.getName());
            existingMember.setEmail(member.getEmail());
            existingMember.setPhone(member.getPhone());
        } else {
            // 비밀번호 암호화
            String encPwd = encoder.encode(rawPwd);
            existingMember.setPwd(encPwd);

            existingMember.setName(member.getName());
            existingMember.setEmail(member.getEmail());
            existingMember.setPhone(member.getPhone());
        }

        memberRepository.save(existingMember);
    }

    @Transactional(readOnly = true)
    public Members findMemberByNum(int num) {
        Optional<Members> member = memberRepository.findById(num);
        return member.orElse(null);
    }
}
