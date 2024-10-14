package com.book.gobook.service;

import com.book.gobook.model.Books;
import com.book.gobook.model.Members;
import com.book.gobook.model.WishList;
import com.book.gobook.repository.BookRepository;
import com.book.gobook.repository.WishListRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class WishListService {
    @Autowired
    private WishListRepository wishListRepository;

    @Autowired
    private BookRepository bookRepository;

    // 위시리스트에 새로운 항목을 추가하는 메서드
    @Transactional
    public void addToWishList(WishList wishList) {
        wishListRepository.save(wishList);
    }




    // 특정 회원의 위시리스트를 조회하는 메서드
    @Transactional
    public List<WishList> findWishListsByMemberId(String memberId) {
        return wishListRepository.findByMemberId((memberId));
    }


    public List<WishList> findByMemberId(String memberId) {
        return wishListRepository.findByMemberId(memberId);
    }

    public void addWishList(WishList wishList) {
        System.out.println("wishList = " + wishList);
        System.out.println("wishList.getBook() = " + wishList.getBook().getNum());
        System.out.println("wishList.getMember() = " + wishList.getMember().getNum());
        System.out.println("wishList.getId() = " + wishList.getId());
        wishListRepository.save(wishList);
    }

    public Object getWishListByMemberId(String memberId) {
        return wishListRepository.findByMemberId(memberId); }

    @Transactional
    public WishList saveWishList(WishList wishList) {
        // Books 엔티티를 먼저 저장합니다.
        Books savedBook = bookRepository.save(wishList.getBook());
        // 저장된 Books 엔티티를 WishList와 연결합니다.
        wishList.setBook(savedBook);
        // 이제 WishList 엔티티를 저장합니다.
        return wishListRepository.save(wishList);
    }

    public WishList findWishListsByMemberNumAndBookNum(Members member, Books book) {
        return wishListRepository.findByMemberNumAndBookNum(member.getNum(), book.getNum());
    }

    public WishList findWishListsByBookNum(Books book) {
        return wishListRepository.findByBookNum(book.getNum());
    }


    public void deleteWishList(WishList wishListDelete) {
        wishListRepository.delete(wishListDelete);
    }

    public WishList findWishListsById(Long id) {
        return wishListRepository.findById(id).orElse(null);
    }
}
