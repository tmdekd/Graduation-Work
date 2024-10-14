package com.book.gobook.repository;

import com.book.gobook.model.Members;
import com.book.gobook.model.PurchaseHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PurchaseHistoryRepository  extends JpaRepository<PurchaseHistory, Long> {
    List<PurchaseHistory> findByMemberAndPurchaseDate(Members member, String purchaseDate);

    List<PurchaseHistory> findByMember(Members member);

//    List<PurchaseHistory> findByMemberAndPurchaseDateBetween(Members member, LocalDateTime startOfDay, LocalDateTime endOfDay);
}
