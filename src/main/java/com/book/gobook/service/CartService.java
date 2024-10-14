package com.book.gobook.service;

import com.book.gobook.model.Books;
import com.book.gobook.model.Cart;
import com.book.gobook.model.Members;
import com.book.gobook.repository.BookRepository;
import com.book.gobook.repository.CartRepository;
import com.book.gobook.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.awt.print.Book;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private MemberRepository memberRepository;

    public void addCart(Members member, Books book) {
        Cart cart = new Cart();
        Cart tmp = cartRepository.findByMemberAndBook(member, book);
        int quantity = 0;
        if(tmp == null) {
            quantity = 1;
            cart.setBook(book);
            cart.setMember(member);
            cart.setQuantity(quantity);
            cartRepository.save(cart);

        } else {
            quantity = tmp.getQuantity();
            tmp.setBook(book);
            tmp.setMember(member);
            tmp.setQuantity(++quantity);
            cartRepository.save(tmp);
        }
    }

    public List<Cart> findCartByMember(Members member) {
        return cartRepository.findByMember(member);
    }

    public void deleteBookFromCart(Cart cartTmp) {
        cartRepository.delete(cartTmp);
    }

    public Cart findCartByBookAndMember(long bookNum, int memberNum) {
        return cartRepository.findByBookNumAndMemberNum(bookNum, memberNum);
    }

    public void updateCartQuantity(Members member, Books book, Integer quantity) {
        Cart cartItem = cartRepository.findCartByMemberAndBook(member, book);

        if (cartItem != null) {
            cartItem.setQuantity(quantity);
            cartRepository.save(cartItem);
        } else {
            throw new RuntimeException("해당 도서가 장바구니에 없습니다.");
        }
    }

    public void addBookToCart(Members member, String title, Integer quantity) {
        Books book = bookRepository.findByTitle(title);

        if (book != null) {
            Cart newCartItem = new Cart();
            newCartItem.setMember(member);
            newCartItem.setBook(book);
            newCartItem.setQuantity(quantity);

            cartRepository.save(newCartItem);
        } else {
            throw new RuntimeException("The book is not found.");
        }
    }

    public void removeBookFromCart(String memberId, String title) {
        // memberRepository에서 memberId에 해당하는 회원을 찾음
        Optional<Members> memberOptional = memberRepository.findById(memberId);

        // 회원이 존재하면
        if (memberOptional.isPresent()) {
            // 장바구니 데이터를 관리하는 저장소에서 해당 회원의 장바구니를 찾음
            List<Cart> carts = cartRepository.findByMember(memberOptional.get());

            // 장바구니가 존재하면
            if (carts != null) {
                Books bookToRemove = null;
                Cart cartToUpdate = null;

                // 각 장바구니에서 삭제할 도서를 찾음
                for (Cart cart : carts) {
                    Books book = cart.getBook();
                    if (book.getTitle().equals(title)) {
                        bookToRemove = book;
                        cartToUpdate = cart;
                        break;
                    }

                    if (bookToRemove != null) {
                        break;
                    }
                }
                System.out.println("cartToUpdate = " + cartToUpdate);
                System.out.println("bookToRemove = " + bookToRemove);
                // 도서가 장바구니에 있으면 삭제
                if (bookToRemove != null && cartToUpdate != null) {
                    cartRepository.delete(cartToUpdate);
                }
            }
        }
    }


}
