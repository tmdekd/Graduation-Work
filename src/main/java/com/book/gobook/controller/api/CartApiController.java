package com.book.gobook.controller.api;

import com.book.gobook.dto.*;
import com.book.gobook.model.Cart;
import com.book.gobook.model.Members;
import com.book.gobook.service.BookService;
import com.book.gobook.service.CartService;
import com.book.gobook.service.MemberService;
import com.book.gobook.service.PurchaseHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.geom.Arc2D;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class CartApiController {
    @Autowired
    private CartService cartService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private BookService bookService;
    @Autowired
    private PurchaseHistoryService purchaseHistoryService;

    // 장바구니 항목 삭제 처리
    @PostMapping("/auth/deleteBook")
    public ResponseDto<Map<String, Object>> deleteBookFromCart(@RequestBody Cart cart) {
        System.out.println("cart.getBook().getNum() = " + cart.getBook().getNum());
        System.out.println("cart.getMember().getNum() = " + cart.getMember().getNum());
        Map<String, Object> responseData = new HashMap<>();
        try {
            Cart cart_tmp = cartService.findCartByBookAndMember(cart.getBook().getNum(), cart.getMember().getNum());
            System.out.println("cart_tmp.getId() = " + cart_tmp.getId());
            if (cart_tmp != null) {
                cartService.deleteBookFromCart(cart_tmp);
                System.out.println("cart.getMember().getId()); = " + cart.getMember().getId());

                // 사용자 정보 추가
                Members member = memberService.findMemberByNum(cart.getMember().getNum());
                responseData.put("userId", member.getId());
                responseData.put("status", 1);
                return new ResponseDto<>(HttpStatus.OK.value(), responseData);
            } else {
                log.error("장바구니 항목 삭제 실패: 장바구니 항목을 찾을 수 없음");
                responseData.put("status", -1); // 실패 상태 코드
                return new ResponseDto<>(HttpStatus.BAD_REQUEST.value(), responseData);
            }
        } catch (Exception e) {
            log.error("장바구니 항목 삭제 실패", e);
            responseData.put("status", -1); // 실패 상태 코드
            return new ResponseDto<>(HttpStatus.BAD_REQUEST.value(), responseData);
        }
    }

    @PostMapping("/auth/payments/prepare")
    public ResponseDto<Integer> PayBook(@RequestBody PayPrepareInfo payPrepareInfo) {
        System.out.println("payPrepareInfo = " + payPrepareInfo);
        System.out.println("payPrepareInfo.getPayCart().toString() = " + payPrepareInfo.getParameters().toString());
        System.out.println("payPrepareInfo.getPayCart().getCheckedBooks().toString() = " + payPrepareInfo.getParameters().getCheckedBooks().toString());
        System.out.println("payPrepareInfo.getParameters().getMemberId() = " + payPrepareInfo.getParameters().getMemberId());
        String userId = payPrepareInfo.getParameters().getMemberId();

        Members member = memberService.findById(userId);
        List<Cart> cartList = cartService.findCartByMember(member);

        List<CheckedBook> checkedBooks = payPrepareInfo.getParameters().getCheckedBooks();
        for(CheckedBook checkedBook : checkedBooks) {
            System.out.println("Book Title: " + checkedBook.getTitle());
        }

        double totalAmountCheckedBooks = payPrepareInfo.getParameters().getCheckedBooks().stream()
                .mapToDouble(checkedBook -> checkedBook.getAmount())
                .sum();

        double totalAmountCartList = cartList.stream()
                .mapToDouble(cart -> cart.getBook().getPrice() * cart.getQuantity())
                .sum();

        if (totalAmountCheckedBooks != totalAmountCartList) {
            return new ResponseDto<Integer>(HttpStatus.BAD_REQUEST.value(), -1);
        }

        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
    }

    @PostMapping("/auth/cart/editBook")
    public ResponseDto<Integer> editListFromCart(@RequestBody EditCart editCart) {
        String memberId = editCart.getParameters().getMemberId();
        Members member = memberService.findById(memberId); // Member 조회
        List<Cart> currentCartList = cartService.findCartByMember(member); // 현재 사용자의 장바구니 목록 조회

        List<CheckedBook> checkedBooks = editCart.getParameters().getCheckedBooks();
        boolean allBooksInCart = checkedBooks.stream()
                .allMatch(checkedBook -> currentCartList.stream()
                        .anyMatch(cart -> cart.getBook().getTitle().equals(checkedBook.getTitle())));

        if (!allBooksInCart) {
            return new ResponseDto<Integer>(HttpStatus.BAD_REQUEST.value(), -1);
        }

        for (CheckedBook checkedBook : checkedBooks) {
            System.out.println("Book Title: " + checkedBook.getTitle());
            for (Cart cart : currentCartList) {
                System.out.println("cart.getBook().getTitle() = " + cart.getBook().getTitle());
                System.out.println("checkedBook.getTitle() = " + checkedBook.getTitle());
                if (cart.getBook().getTitle().equals(checkedBook.getTitle())) {
                    System.out.println("checkedBook.getQuantity() = " + checkedBook.getQuantity());
                    if (cart.getQuantity() != checkedBook.getQuantity()) {
                        // 수량이 다르면 업데이트
                        cartService.updateCartQuantity(member, cart.getBook(), checkedBook.getQuantity());
                    }
                }
            }
        }

        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
    }

    @PostMapping("/auth/payments/after")
    public ResponseDto<Integer> afterPay(@RequestBody EditCart editCart) {
        System.out.println("editCart.getParameters().getMemberId() = " + editCart.getParameters().getMemberId());
        String memberId = editCart.getParameters().getMemberId();
        Members member = memberService.findById(memberId);

        List<CheckedBook> checkedBooks = editCart.getParameters().getCheckedBooks();

        for (CheckedBook checkedBook : checkedBooks) {
            cartService.removeBookFromCart(memberId, checkedBook.getTitle());
            bookService.increaseSales(checkedBook.getTitle(), checkedBook.getQuantity());
            int totalPrice = checkedBook.getQuantity() * checkedBook.getPrice();
            purchaseHistoryService.createPurchaseHistory(memberId, checkedBook.getTitle(), checkedBook.getQuantity(), totalPrice); // 수정된 메서드 호출
        }

        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
    }
}
