package com.book.gobook.repository;

import com.book.gobook.model.Books;
import com.book.gobook.model.Cart;
import com.book.gobook.model.Members;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByMember(Members member);
    Cart findByMemberAndBook(Members member, Books book);

    Cart findByBookNumAndMemberNum(long bookNum, int memberNum);

    Cart findCartByMemberAndBook(Members member, Books book);
}
