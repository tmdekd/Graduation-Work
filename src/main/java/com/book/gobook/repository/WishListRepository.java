package com.book.gobook.repository;

import com.book.gobook.model.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishListRepository extends JpaRepository<WishList, Long> {
    List<WishList> findByMemberId(String memberId);

    WishList findByMemberNumAndBookNum(int num, long num1);

    WishList findByBookNum(long num);
}