package com.book.gobook.repository;

import com.book.gobook.model.Books;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Books, Long> {
    // Containg을 사용하면 sql의 LIKE 키워드와 같은 역할
    List<Books> findByTitleContaining(String keyword);
    Books findByUid(String uid);
    Page<Books> findAllByOrderBySalesDescTitleAsc(Pageable pageable);
    List<Books> findByCategory(String category);
    Books findByTitle(String title);
}
