package com.ecommerce.project.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RazorpayPaymentDto {

    private String orderId;
    private Long amount;
    private String currency;
    private String status;
    private String receipt;
}