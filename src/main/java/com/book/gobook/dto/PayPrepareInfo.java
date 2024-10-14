package com.book.gobook.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PayPrepareInfo {
    private String merchant_uid;
    private PayCart parameters;
}
