package com.ecommerce.project.service;

import com.ecommerce.project.payload.RazorpayPaymentDto;
import com.ecommerce.project.payload.RazorpayPaymentVerificationDTO;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.Utils;
import jakarta.transaction.Transactional;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class RazorpayServiceImpl implements RazorpayService {

    @Value("${razorpay.key.id}")
    private String razorpayKeyId;

    @Value("${razorpay.key.secret}")
    private String razorpayKeySecret;

    @Override
    public RazorpayPaymentDto createRazorpayOrder(
            RazorpayPaymentDto razorpayPaymentDto)
            throws Exception {

        RazorpayClient razorpayClient =
                new RazorpayClient(
                        razorpayKeyId,
                        razorpayKeySecret
                );

        JSONObject orderRequest = new JSONObject();

        orderRequest.put(
                "amount",
                razorpayPaymentDto.getAmount()
        );

        orderRequest.put(
                "currency",
                razorpayPaymentDto.getCurrency()
        );

        orderRequest.put(
                "receipt",
                razorpayPaymentDto.getReceipt()
        );

        Order razorpayOrder =
                razorpayClient.orders.create(orderRequest);

        razorpayPaymentDto.setOrderId(
                (String) razorpayOrder.get("id")
        );

        razorpayPaymentDto.setStatus(
                (String) razorpayOrder.get("status")
        );

        Number amount =
                (Number) razorpayOrder.get("amount");

        razorpayPaymentDto.setAmount(
                amount.longValue()
        );

        razorpayPaymentDto.setCurrency(
                (String) razorpayOrder.get("currency")
        );

        razorpayPaymentDto.setReceipt(
                (String) razorpayOrder.get("receipt")
        );

        return razorpayPaymentDto;
    }

    @Override
    public boolean verifyPayment(
            RazorpayPaymentVerificationDTO paymentVerificationDTO)
            throws Exception {

        String payload =
                paymentVerificationDTO.getRazorpayOrderId()
                        + "|"
                        + paymentVerificationDTO.getRazorpayPaymentId();

        String generatedSignature =
                Utils.getHash(
                        payload,
                        razorpayKeySecret
                );

        return generatedSignature.equals(
                paymentVerificationDTO.getRazorpaySignature()
        );
    }
}