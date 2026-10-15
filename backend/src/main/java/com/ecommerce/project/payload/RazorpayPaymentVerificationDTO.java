package com.ecommerce.project.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RazorpayPaymentVerificationDTO {

    private String razorpayOrderId;
    private String razorpayPaymentId;
    private String razorpaySignature;

    private Long addressId;
}
