package com.book.gobook.controller;


import com.book.gobook.dto.ResetPasswordRequest;
import com.book.gobook.model.*;
import com.book.gobook.repository.CartRepository;
import com.book.gobook.service.*;
import com.book.gobook.token.TokenGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.book.gobook.dto.ExitDto;


import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/auth")
public class MemberController {
    @Autowired
    private MemberService memberService;
    @Autowired
    private BookService bookService;
    @Autowired
    private CartService cartService;
    @Autowired
    private JavaMailSender emailSender;
    @Autowired
    private TokenGenerator tokenGenerator;
    @Autowired
    private ResetPasswordService resetPasswordService;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private PurchaseHistoryService purchaseHistoryService;

    private String qrCode;

    @GetMapping("/login_joinForm")
    public String login_joinForm() {
        return "member/login_join";
    }

    @GetMapping("/reset_pwd")
    public String reset_pwd() {
        return "member/reset_pwd";
    }

    @PostMapping("/send_reset_email")
    public String sendResetEmail(@RequestBody ResetPasswordRequest request, Model model) {
        System.out.println("sendResetEmail 메서드 실행");
        Members member = memberService.findMemberByIdAndEmail(request.getId(), request.getEmail());
        System.out.println("request.getId() = " + request.getId());
        System.out.println("request.getEmail() = " + request.getEmail());

        if (member != null) {
            System.out.println("member = " + member);
            String token = tokenGenerator.generateTokenWithExpiry();
            resetPasswordService.createResetPasswordToken(member.getNum(), token);

            String tempLink = "http://localhost:9701/auth/reset_password?token=" + token;

            // 이메일 전송 로직
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("noreply@yourwebsite.com");
            message.setTo(request.getEmail());
            message.setSubject("비밀번호 재설정 링크");
            message.setText("비밀번호 재설정을 위한 링크입니다: " + tempLink);
            emailSender.send(message);

            model.addAttribute("message", "비밀번호 재설정 링크가 이메일로 전송되었습니다.");
        } else {
            model.addAttribute("message", "입력하신 정보와 일치하는 회원 정보가 없습니다.");
        }

        return "member/login_join";
    }

    @GetMapping("/reset_password")
    public String resetPasswordPage(@RequestParam("token") String token, Model model) {
        ResetPasswordToken resetToken = resetPasswordService.validateResetPasswordToken(token);
        System.out.println("token = " + token);
        if (resetToken != null) {
            System.out.println("resetToken = " + resetToken);
            model.addAttribute("token", "인증된 토큰");
        }
        return "member/reset_pwd_link";
    }

    @PostMapping("/qrCodeSend")
    public ResponseEntity<Map<String, Object>> checkMember(@RequestBody Map<String, String> qrCodeData) {
        Map<String, Object> response = new HashMap<>();
        System.out.println("checkMember 메서드 실행");
        qrCode = qrCodeData.get("qr_code");
        System.out.println("qrCode = " + qrCode);

        Members member = memberService.findMemberByQrCode(qrCode);
        if (member != null) {
            System.out.println("member = " + member.toString());
            response.put("message", "OPEN");
        } else {
            System.out.println("일치하는 회원 정보가 없습니다.");
            response.put("message", "CLOSE");
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/qrMemberLogin")
    public String qrMemberLogin(@RequestParam(value = "qrCode", defaultValue = "-1") String qrCode,
                                @RequestParam(value = "uid", defaultValue = "-2") String uid, Model model) {
        System.out.println("qrMemberLogin 메서드 실행");
        System.out.println("qrCode = " + qrCode);
        System.out.println("uid = " + uid);

        try {
            if ("-1".equals(qrCode)) {
                model.addAttribute("errorMessage", "회원 QR코드를 스캔해주세요.");
                return "member/wait";
            } else if(("-1".equals(qrCode) && !"-2".equals(uid)) || ("-2".equals(uid) && !"-1".equals(qrCode))) {
                Members member = memberService.findMemberByQrCode(qrCode);

                if (member != null) {
                    // 해당 회원의 장바구니 페이지 보여주기
                    List<Cart> cartList = cartService.findCartByMember(member);
                    model.addAttribute("userPrincipal", member);
                    model.addAttribute("userId", member.getId());

                    model.addAttribute("cartList", cartList);

                    // 장바구니 페이지로 이동
                    return "board/cart";

                } else {
                    System.out.println("일치하는 회원 정보가 없습니다.");
                    model.addAttribute("errorMessage", "일치하는 회원 정보가 없습니다.");
                    return "member/wait";
                }
            } else if(!"-1".equals(qrCode) && !"-2".equals(uid)) {
                try {
                    System.out.println("uid 인식 시작");
                    Members member = memberService.findMemberByQrCode(qrCode);
                    Books book = bookService.searchByUid_admin(uid);
                    if (book != null && member != null) {
                        System.out.println("book = " + book);
                        cartService.addCart(member, book);
                        // 해당 회원의 장바구니 내역을 조회하여 보여주는 로직 추가
                        List<Cart> cartList = cartRepository.findByMember(member);

                        model.addAttribute("userPrincipal", member);
                        model.addAttribute("cartList", cartList);

                        return "board/cart";
                    } else {
                        model.addAttribute("errorMessage", "도서 조회 과정에서 book 객체의 값이 null입니다.");
                    }
                } catch (Exception e) {
                    model.addAttribute("errorMessage", "도서 조회 과정에서 오류가 발생했습니다: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "오류가 발생했습니다: " + e.getMessage());
        }

        return "member/wait";
    }

    @PostMapping("/purchaseCheckQrCode")
    public ResponseEntity<Map<String, Object>> checkMemberPurchase(@RequestBody ExitDto exitDto) {
        Map<String, Object> response = new HashMap<>();
        System.out.println("checkMember 메서드 실행");
        String qrCode = exitDto.getQr_code();
        String epc = exitDto.getEpc();

        if (!"".equals(qrCode) && !"".equals(epc)) {
            System.out.println("qrCode = " + qrCode);
            System.out.println("epc = " + epc);

            Members member = memberService.findMemberByQrCode(qrCode);
            if (member != null) {
                System.out.println("member = " + member.toString());

                String currentDate = LocalDate.now().toString();
                System.out.println("currentDate = " + currentDate);
                List<PurchaseHistory> purchaseHistories = purchaseHistoryService.findPurchaseHistoryByMemberAndDate(member, currentDate);

                if (purchaseHistories != null && !purchaseHistories.isEmpty()) {
                    boolean epcFound = false;
                    for (PurchaseHistory purchaseHistory : purchaseHistories) {
                        if (purchaseHistory.getBook().getUid().equals(epc)) {
                            epcFound = true;
                            break;
                        }
                    }
                    if (epcFound) {
                        System.out.println("EPC 값이 구매한 도서의 UID에 포함되어 있습니다.");
                        response.put("message", "OPEN");
                    } else {
                        System.out.println("EPC 값이 구매한 도서의 UID에 포함되어 있지 않습니다.");
                        response.put("message", "CLOSE");
                    }
                } else {
                    System.out.println("구매 이력이 없으며 EPC 값이 존재합니다. 도난 상황으로 판단됩니다.");
                    response.put("message", "CLOSE");
                }
            } else {
                System.out.println("일치하는 회원 정보가 없습니다.");
                response.put("message", "CLOSE");
            }
        } else if("".equals(epc) && !"".equals(qrCode)) {
            System.out.println("qrCode 값은 존재하지만 epc 값이 누락되었습니다.");
            response.put("message", "OPEN");
        } else {
            System.out.println("qrCode 또는 epc 값이 누락되었습니다.");
            response.put("message", "CLOSE");
        }
        return ResponseEntity.ok(response);
    }


    @GetMapping("/In")
    public String In_Cor(Model model, @RequestParam(value = "qrCode") String qrCode) {
        System.out.println("\nIn_Cor 메서드 실행");
        System.out.println("========================================");
        System.out.println("qrCode = " + qrCode);
        System.out.println("========================================");

        Members member = memberService.findMemberByQrCode(qrCode);

        if (member != null) {
            System.out.println("member = " + member.toString());
            model.addAttribute("member", member);
        } else {
            model.addAttribute("message", "일치하는 회원 정보가 없습니다.");
        }
        return "member/in";
    }

    @GetMapping("/Out")
    public String Out_Cor(Model model, @RequestParam(value = "qrCode") String qrCode, @RequestParam(value = "sit_val") String sit_val) {
        System.out.println("\nOut_Cor 메서드 실행");

        Members member = memberService.findMemberByQrCode(qrCode);
        System.out.println("========================================");
        System.out.println("qrCode = " + qrCode);
        System.out.println("sit_val = " + sit_val);
        System.out.println("sit_val.getClass().getTypeName() = " + sit_val.getClass().getTypeName());
        System.out.println("========================================");

        if (sit_val.equals("0") && member != null) {
            System.out.println("member = " + member.toString());
            model.addAttribute("member", member);
        } else if(sit_val.equals("1") && member != null) {
            System.out.println("member = " + member.toString());
            model.addAttribute("member", member);
            return "member/theft_situation";
        } else {
            model.addAttribute("message", "일치하는 회원 정보가 없습니다. 다시 인식시켜주세요.");
        }
        return "member/out";
    }

    @GetMapping("/Wait")
    public String Wait() {
        return "member/wait";
    }
}