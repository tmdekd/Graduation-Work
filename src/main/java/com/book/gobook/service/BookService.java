package com.book.gobook.service;

import com.book.gobook.model.Books;
import com.book.gobook.repository.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.awt.print.Book;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    public List<Books> searchBooks(String keyword) {
        return bookRepository.findByTitleContaining(keyword);
    }
    public Books searchByUid(String uid) {
        Books book = bookRepository.findByUid(uid);
        System.out.println("uid = " + uid);
        if (book != null) {
            if(book.getCnt() > 0) {
                book.setCnt(book.getCnt() - 1);
                book.setCnt_noCategory(book.getCnt_noCategory() + 1);
                System.out.println("book = " + book);
                bookRepository.save(book);
            } else {
                System.out.println("모든 책이 무카테고리 책장에 존재하는 상황");
            }
        }
        return book;
    }

    public Books searchByUid_admin(String uid) {
        Books book = bookRepository.findByUid(uid);
        System.out.println("uid = " + uid);

        return book;
    }

    public List<Books> findBestSellers() {
        return bookRepository.findAllByOrderBySalesDescTitleAsc(PageRequest.of(0, 5)).getContent();
    }
    public List<Books> findNovels() {
        return bookRepository.findByCategory("소설");
    }
    public List<Books> findHistoriesCultures() {
        return bookRepository.findByCategory("역사/문화");
    }
    public List<Books> findPersonalDevelopment() {
        return bookRepository.findByCategory("자기계발");
    }

    public void increaseSales(String title, Integer quantity) {
        // 도서 정보를 제목으로 조회
        Books book = bookRepository.findByTitle(title);
        if (book != null) {
            // 현재 판매량을 증가시킴
            book.setSales(book.getSales() + quantity);
            // 변경된 도서 정보를 저장
            bookRepository.save(book);
        } else {
            throw new IllegalArgumentException("해당 제목의 도서를 찾을 수 없습니다: " + title);
        }
    }

    public Books findByTitle(String title) {
        return bookRepository.findByTitle(title);
    }
}
