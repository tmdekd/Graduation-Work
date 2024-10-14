package com.book.gobook.dto;

import com.book.gobook.model.Books;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookWrapper {
    private Books book;
    private int index;
}
