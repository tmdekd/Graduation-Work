package com.book.gobook.service;

import com.book.gobook.model.Books;
import com.book.gobook.model.Members;
import com.book.gobook.model.PurchaseHistory;
import com.book.gobook.repository.PurchaseHistoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
public class PurchaseHistoryService {
    @Autowired
    private PurchaseHistoryRepository purchaseHistoryRepository;
    @Autowired
    private MemberService memberService;
    @Autowired
    private BookService bookService;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public List<PurchaseHistory> findByMemberAndPurchaseDate(Members member, String purchaseDate) {
        System.out.println("member.getQrCode() = " + member.getQrCode());
        System.out.println("member.getName() = " + member.getName());
        System.out.println("Purchase Date: " + purchaseDate);

        // purchaseHistoryRepository로부터 결과를 받음
        List<PurchaseHistory> purchaseHistories = purchaseHistoryRepository.findByMemberAndPurchaseDate(member, purchaseDate);

        // 조회된 구매 이력 출력
        if (purchaseHistories != null && !purchaseHistories.isEmpty()) {
            System.out.println("조회된 구매 이력:");
            for (PurchaseHistory purchaseHistory : purchaseHistories) {
                System.out.println(purchaseHistory); // PurchaseHistory 객체의 toString() 메서드를 적절히 오버라이드 했다고 가정
            }
        } else {
            System.out.println("구매 이력이 없습니다.");
        }

        return purchaseHistories;
    }

    public void createPurchaseHistory(String memberId, String bookTitle, Integer quantity, Integer totalPrice) {
        Members member = memberService.findById(memberId);
        Books book = bookService.findByTitle(bookTitle);
        if (book == null) {
            log.error("책 정보를 찾을 수 없습니다.");
            return;
        } else {
            PurchaseHistory purchaseHistory = new PurchaseHistory();
            purchaseHistory.setBook(book);
            purchaseHistory.setAuthenticationStatus("O");
            purchaseHistory.setMember(member);
            purchaseHistory.setQuantity(quantity);
            purchaseHistory.setTotalPrice(totalPrice);

            // 한국 시간대에 맞춰 현재 날짜와 시간을 설정
            LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));

            // 날짜와 시간 정보를 각각 분리하여 설정
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

            String formattedDate = now.format(dateFormatter);
            String formattedTime = now.format(timeFormatter);

            purchaseHistory.setPurchaseDate(formattedDate); // 날짜 정보 설정
            purchaseHistory.setPurchaseTime(formattedTime); // 시간 정보 설정

            purchaseHistoryRepository.save(purchaseHistory);
        }
    }

    public List<PurchaseHistory> findPurchaseHistoryByMemberAndDate(Members member, String date) {
        return purchaseHistoryRepository.findByMemberAndPurchaseDate(member, date);
    }

//    public List<PurchaseHistory> findByMemberAndPurchaseDateBetween(Members member, LocalDateTime startOfDay, LocalDateTime endOfDay) {
//        return purchaseHistoryRepository.findByMemberAndPurchaseDateBetween(member, startOfDay, endOfDay);
//    }
}
