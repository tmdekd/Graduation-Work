package com.book.gobook.controller;

import com.book.gobook.config.auth.PrincipalDetail;
import com.book.gobook.dto.BookWrapper;
import com.book.gobook.model.Books;
import com.book.gobook.model.Members;
import com.book.gobook.model.PurchaseHistory;
import com.book.gobook.model.WishList;
import com.book.gobook.service.BookService;
import com.book.gobook.service.MemberService;
import com.book.gobook.service.PurchaseHistoryService;
import com.book.gobook.service.WishListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
public class BoardController {
    @Autowired
    private BookService bookService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private PurchaseHistoryService purchaseHistoryService;
    @Autowired
    private WishListService wishListService;

    private String qrCode;

    @GetMapping("/")
    public String index(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 사용자 정보가 있는지 확인, 인증된 사용자인지 확인, 사용자 객체가 PrincipalDetail 타입인지 확인
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof PrincipalDetail) {
            PrincipalDetail principal = (PrincipalDetail) authentication.getPrincipal();
            model.addAttribute("userPrincipal", principal);
        }

        List<Books> bestSellers = bookService.findBestSellers();
        List<BookWrapper> wrappedBestSellers = new ArrayList<>();
        for (int i = 0; i < bestSellers.size(); i++) {
            wrappedBestSellers.add(new BookWrapper(bestSellers.get(i), i + 1));
        }

        List<Books> novels = bookService.findNovels();
        List<Books> histories_cultures = bookService.findHistoriesCultures();
        List<Books> personalDevelopment = bookService.findPersonalDevelopment();

        model.addAttribute("personalDevelopment", personalDevelopment);
        model.addAttribute("histories_cultures", histories_cultures);
        model.addAttribute("novels", novels);
        model.addAttribute("bestSellers", wrappedBestSellers);

        return "index";
    }

    @GetMapping("/auth/logout")
    public String logout() {
        return "redirect:/logout";
    }

    @GetMapping("/mypage")
    public String mypage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 사용자 정보가 있는지 확인, 인증된 사용자인지 확인, 사용자 객체가 PrincipalDetail 타입인지 확인
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof PrincipalDetail) {
            PrincipalDetail principal = (PrincipalDetail) authentication.getPrincipal();
            model.addAttribute("userPrincipal", principal);
        }

        return "/board/showQR";
    }
    @GetMapping("/editMember")
    public String editMember(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 사용자 정보가 있는지 확인, 인증된 사용자인지 확인, 사용자 객체가 PrincipalDetail 타입인지 확인
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof PrincipalDetail) {
            PrincipalDetail principal = (PrincipalDetail) authentication.getPrincipal();
            model.addAttribute("userPrincipal", principal);

            String principal_id = principal.getUsername();
            Members member = memberService.findMemberById(principal_id);

            if (member != null) {
                model.addAttribute("id", member.getId());
                model.addAttribute("phone", member.getPhone());
                model.addAttribute("email", member.getEmail());
                model.addAttribute("name", member.getName());
            } else {
                // 조회된 사용자 정보가 없다면 적절한 처리(예: 에러 메시지 설정)
                model.addAttribute("errorMessage", "사용자 정보를 찾을 수 없습니다.");

            }
        }
        return "/board/editMember";
    }

    @GetMapping("/tweetBook")
    public String tweetBook(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 사용자 정보가 있는지 확인, 인증된 사용자인지 확인, 사용자 객체가 PrincipalDetail 타입인지 확인
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof PrincipalDetail) {
            PrincipalDetail principal = (PrincipalDetail) authentication.getPrincipal();
            model.addAttribute("userPrincipal", principal);

            List<WishList> wishList = wishListService.findByMemberId(principal.getUsername());
            for(WishList item : wishList) {
                System.out.println("Found WishList item: " + item.getId());
            }
            model.addAttribute("wishList", wishList);

        }
        return "/board/tweetBook";
    }

    @GetMapping("/auth/search")
    public String search(@RequestParam("keyword") String keyword, Model model) {
        List<Books> searchList = bookService.searchBooks(keyword);
        model.addAttribute("searchList", searchList);
        model.addAttribute("isSearchEmpty", searchList.isEmpty()); // 검색 결과가 비어있는지 여부를 모델에 추가
        System.out.println("searchList = " + searchList);
        System.out.println("searchList.isEmpty() = " + searchList.isEmpty());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof PrincipalDetail) {
            PrincipalDetail principal = (PrincipalDetail) authentication.getPrincipal();
            model.addAttribute("userPrincipal", principal);
        }

        if(searchList.isEmpty()) {
            return "/board/searchNoList";
        } else {
            return "/board/searchList";
        }

    }

    // 무카테고리 책장 도서 위치 정보 표시
    @GetMapping("/auth/scan_noCategory")
    public String scan_noCategory(@RequestParam(value = "uid", defaultValue = "-1") String uid, Model model) {
        System.out.println("scan_noCategory 메서드 실행");
        try {
            if ("-1".equals(uid)) {
                return "board/scan_noCategory_wait";
            } else {
                Books book = bookService.searchByUid(uid);
                if (book != null) {
                    model.addAttribute("book", book);
                } else {
                    return "board/scan_noCategory_error";
                }
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "도서 조회 과정에서 오류가 발생했습니다: " + e.getMessage());
        }
        return "board/scan_noCategory";
    }

    // 무카테고리 책장 도서 위치 정보 표시 / 관리자 페이지
    @GetMapping("/auth/scan_noCategoryAdmin")
    public String scan_noCategoryAdmin(@RequestParam(value = "uid", defaultValue = "-1") String uid, Model model) {
        System.out.println("scan_noCategoryAdmin 메서드 실행");
        try {
            if ("-1".equals(uid)) {
                System.out.println("관리자 모드 실행");
            } else {
                Books book = bookService.searchByUid_admin(uid);
                if (book != null) {
                    model.addAttribute("book", book);
                } else {
                    return "board/scan_noCategory_error";
                }
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "도서 조회 과정에서 오류가 발생했습니다: " + e.getMessage());
        }
        return "board/scan_noCategory_admin";
    }

    // 소설 페이지 보여주기
    @GetMapping("/auth/book/novel")
    public String showNovel(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 사용자 정보가 있는지 확인, 인증된 사용자인지 확인, 사용자 객체가 PrincipalDetail 타입인지 확인
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof PrincipalDetail) {
            PrincipalDetail principal = (PrincipalDetail) authentication.getPrincipal();
            model.addAttribute("userPrincipal", principal);
        }
        List<Books> novels = bookService.findNovels();
        model.addAttribute("novels", novels);
        return "board/novel";
    }

    // 역사 페이지 보여주기
    @GetMapping("/auth/book/history_culture")
    public String showHistory(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 사용자 정보가 있는지 확인, 인증된 사용자인지 확인, 사용자 객체가 PrincipalDetail 타입인지 확인
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof PrincipalDetail) {
            PrincipalDetail principal = (PrincipalDetail) authentication.getPrincipal();
            model.addAttribute("userPrincipal", principal);
        }
        List<Books> histories_cultures = bookService.findHistoriesCultures();
        model.addAttribute("histories_cultures", histories_cultures);
        return "board/history_culture";
    }

    // 자기계발 페이지 보여주기
    @GetMapping("/auth/book/personalDevelopment")
    public String showPersonalDevelopment(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 사용자 정보가 있는지 확인, 인증된 사용자인지 확인, 사용자 객체가 PrincipalDetail 타입인지 확인
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof PrincipalDetail) {
            PrincipalDetail principal = (PrincipalDetail) authentication.getPrincipal();
            model.addAttribute("userPrincipal", principal);
        }
        List<Books> personalDevelopment = bookService.findPersonalDevelopment();
        model.addAttribute("personalDevelopment", personalDevelopment);
        return "board/personalDevelopment";
    }

    @GetMapping("/noCart")
    public String noCart(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof PrincipalDetail) {
            PrincipalDetail principal = (PrincipalDetail) authentication.getPrincipal();
            model.addAttribute("userPrincipal", principal);
        }
        return "board/noCart";
    }

    @GetMapping("/cart")
    public String cart(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof PrincipalDetail) {
            PrincipalDetail principal = (PrincipalDetail) authentication.getPrincipal();
            model.addAttribute("userPrincipal", principal);
        }
        return "board/cart";
    }

    @GetMapping("/auth/payment/complete")
    public String paymentCompleted(@RequestParam(value = "userId", defaultValue = "-1") String userId, Model model) {
        Members member = null;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof PrincipalDetail) {
            PrincipalDetail principal = (PrincipalDetail) authentication.getPrincipal();
            model.addAttribute("userPrincipal", principal);
            // 로그인한 사용자의 ID를 사용하여 회원 조회
            member = memberService.findById(principal.getUsername());
        }
        // 한국 시간대의 오늘 날짜를 문자열로 구하기
        LocalDate todayDate = LocalDate.now(ZoneId.of("Asia/Seoul"));
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String todayDateString = todayDate.format(dateFormatter);

        // 로그인한 사용자의 ID를 사용하여 회원 조회
        member = memberService.findById(userId);
        if (member != null) {
            model.addAttribute("userPrincipal", member);
            // 회원 정보가 있는 경우 당일 구매 이력 조회
            List<PurchaseHistory> todayPurchaseHistory = purchaseHistoryService.findByMemberAndPurchaseDate(member, todayDateString);

            // 당일 구매 이력에서 총 금액 계산
            int totalAmount = todayPurchaseHistory.stream()
                    .mapToInt(PurchaseHistory::getTotalPrice)
                    .sum();
            model.addAttribute("today", todayDateString);
            model.addAttribute("todayPurchaseHistory", todayPurchaseHistory);
            model.addAttribute("totalAmount", totalAmount); // 총 금액을 모델에 추가

            // 당일 구매 이력과 총 금액 출력
            if (!todayPurchaseHistory.isEmpty()) {
                System.out.println("==== 당일 구매 이력 ====");
                for (PurchaseHistory history : todayPurchaseHistory) {
                    System.out.println("구매 ID: " + history.getId() + ", 상품명: " + history.getBook().getTitle() + ", 구매 금액: " + history.getTotalPrice() + ", 구매 날짜: " + history.getPurchaseDate() + ", 구매 시간: " + history.getPurchaseTime());
                }
                System.out.println("==== 총 금액: " + totalAmount + "원 ====");
            } else {
                System.out.println("오늘의 구매 이력이 없습니다.");
            }
        } else {
            System.out.println("해당 사용자의 회원 정보가 없습니다.");
        }
        return "board/paymentCompleted";
    }

    @GetMapping("/auth/howToUse")
    public String howToUse(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof PrincipalDetail) {
            PrincipalDetail principal = (PrincipalDetail) authentication.getPrincipal();
            model.addAttribute("userPrincipal", principal);
        }
        return "board/howToUse";
    }
}

