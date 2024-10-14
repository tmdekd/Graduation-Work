package com.book.gobook.controller.api;


import com.book.gobook.dto.ResponseDto;
import com.book.gobook.model.WishList;
import com.book.gobook.service.BookService;
import com.book.gobook.service.WishListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Book;

@Slf4j
@RestController
public class WishListApiController {
    @Autowired
    private WishListService wishListService;

    // 도서 찜하기 기능
    @PostMapping("/tweetBook")
    public ResponseDto<Integer> addWishList(@RequestBody WishList wishList) {
        log.info("WishListApiController: add 호출됨");
        WishList wishList_add = wishListService.findWishListsByMemberNumAndBookNum(wishList.getMember(), wishList.getBook());

        // 이미 위시리스트에 존재하는 도서인지 확인
        if (wishList_add != null) {
            //wishList.getBook().getTitle();
            //String title = wishList.getBook().getTitle();
            // 이미 위시리스트에 존재하는 경우, 에러 메시지와 함께 실패 응답 반환
            return new ResponseDto<Integer>(HttpStatus.BAD_REQUEST.value(), -1); // 이미 위시리스트에 추가된 도서입니다.
        } else {

            // 위시리스트에 없는 경우, 위시리스트에 추가
            wishListService.addWishList(wishList);
            // 성공적으로 추가됨
            return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
        }

    }

    @PostMapping("/tweetBook/delete")
    public ResponseDto<Integer> deleteWishList(@RequestBody WishList wishList) {
        log.info("WishListApiController: delete 호출됨");
        WishList wishListToDelete = wishListService.findWishListsById(wishList.getId());

        if (wishListToDelete != null) {
            wishListService.deleteWishList(wishListToDelete);
            log.info("찜 목록에서 도서가 성공적으로 삭제됨");
            return new ResponseDto<>(HttpStatus.OK.value(), 1); // 성공적으로 삭제됨
        } else {
            log.error("찜 목록에서 해당 도서를 찾을 수 없음");
            return new ResponseDto<>(HttpStatus.BAD_REQUEST.value(), -1); // 찜 목록에 해당 도서가 없음
        }
    }


}