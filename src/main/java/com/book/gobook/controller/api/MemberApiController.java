package com.book.gobook.controller.api;

import com.book.gobook.dto.ResponseDto;
import com.book.gobook.model.Members;
import com.book.gobook.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
public class MemberApiController {
    @Autowired
    private MemberService memberService;

    @PostMapping("/joinProc")
    public ResponseDto<Integer> save(@RequestBody Members members) {
        System.out.println("MemberApiController: join 호출됨");
        log.info(members.toString());
        memberService.join(members);
        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
    }

    @GetMapping("/checkUserId")
    public boolean checkUserId(@RequestParam String userId) {
        log.info("Checking user ID: {}", userId);
        return memberService.isUserIdAvailable(userId);
    }

    @PostMapping("/resetPwd")
    public ResponseDto<Object> resetPwd(@RequestBody Members members, HttpServletRequest request) {
        System.out.println("MemberController: resetPwd 호출됨");
        log.info("요청된 회원 정보: " + members.toString());

        String memberId = members.getId();
        String newPwd = members.getPwd();

        try {
            Members member = memberService.findById(memberId);
            if (member != null) {
                member.setPwd(newPwd);
                memberService.save(member);
                log.info("비밀번호 재설정 성공: " + memberId);

                request.getSession().invalidate();
                request.getSession(true);

                return new ResponseDto<Object>(HttpStatus.OK.value(), 1);
            } else {
                log.error("비밀번호 재설정 실패: 회원을 찾을 수 없음 - " + memberId);
                return new ResponseDto<Object>(HttpStatus.BAD_REQUEST.value(), "비밀번호 재설정 실패: 회원을 찾을 수 없음");
            }
        } catch (Exception e) {
            log.error("비밀번호 재설정 중 오류 발생", e);
            return new ResponseDto<Object>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 내부 오류로 인한 비밀번호 재설정 실패");
        }
    }

    @PostMapping("/updateMember")
    public ResponseDto<Integer> update(@RequestBody Members member) {
        System.out.println("MemberApiController: update 호출됨");
        log.info(member.toString());

        memberService.updateMember(member);
        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
    }
}
