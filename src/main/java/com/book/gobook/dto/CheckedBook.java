package com.book.gobook.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckedBook {
    private String title;
    private Integer price;
    private Integer quantity;
    private Integer amount;
}