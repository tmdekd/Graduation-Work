package com.book.gobook.dto;

import lombok.*;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PayCart {
    private List<CheckedBook> checkedBooks;
    private Double totalAmount;
    private Integer checkedBooksLength;
    private String memberId;
    private String memberName;
    private String memberEmail;
    private String memberPhone;
    private String impName;
    private String paid_at;
    private String makeMerchantUid;
}