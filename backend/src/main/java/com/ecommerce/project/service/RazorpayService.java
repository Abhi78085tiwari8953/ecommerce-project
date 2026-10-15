package com.ecommerce.project.service;

import com.ecommerce.project.payload.RazorpayPaymentDto;
import com.ecommerce.project.payload.RazorpayPaymentVerificationDTO;

public interface RazorpayService {

    RazorpayPaymentDto createRazorpayOrder(
            RazorpayPaymentDto razorpayPaymentDto
    ) throws Exception;

    boolean verifyPayment(
            RazorpayPaymentVerificationDTO paymentVerificationDTO
    ) throws Exception;
}