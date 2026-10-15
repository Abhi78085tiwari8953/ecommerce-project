package com.ecommerce.project.controller;

import com.ecommerce.project.config.AppConstants;
import com.ecommerce.project.payload.*;
import com.ecommerce.project.service.OrderService;
import com.ecommerce.project.service.RazorpayService;
import com.ecommerce.project.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ecommerce.project.payload.RazorpayPaymentVerificationDTO;

@RestController
@RequestMapping("/api")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private AuthUtil authUtil;

    @Autowired
    private RazorpayService razorpayService;


    // =========================
    // PLACE ORDER
    // =========================

    @PostMapping("/order/users/payments/{paymentMethod}")
    public ResponseEntity<OrderDTO> orderProducts(
            @PathVariable String paymentMethod,
            @RequestBody OrderRequestDTO orderRequestDTO) {

        String emailId = authUtil.loggedInEmail();

        System.out.println(
                "orderRequestDTO DATA: "
                        + orderRequestDTO
        );

        OrderDTO order = orderService.placeOrder(
                emailId,
                orderRequestDTO.getAddressId(),
                paymentMethod,
                orderRequestDTO.getPgName(),
                orderRequestDTO.getPgPaymentId(),
                orderRequestDTO.getPgStatus(),
                orderRequestDTO.getPgResponseMessage()
        );

        return new ResponseEntity<>(
                order,
                HttpStatus.CREATED
        );
    }


    // =========================
    // CREATE RAZORPAY ORDER
    // =========================

    @PostMapping("/order/razorpay")
    public ResponseEntity<RazorpayPaymentDto> createRazorpayOrder(
            @RequestBody RazorpayPaymentDto razorpayPaymentDto)
            throws Exception {

        System.out.println(
                "RazorpayPaymentDTO Received "
                        + razorpayPaymentDto
        );

        RazorpayPaymentDto response =
                razorpayService.createRazorpayOrder(
                        razorpayPaymentDto
                );

        return new ResponseEntity<>(
                response,
                HttpStatus.CREATED
        );
    }

    @PostMapping("/order/razorpay/verify")
    public ResponseEntity<String> verifyRazorpayPayment(
            @RequestBody RazorpayPaymentVerificationDTO paymentVerificationDTO)
            throws Exception {

        boolean isValid =
                razorpayService.verifyPayment(
                        paymentVerificationDTO
                );

        if (isValid) {
            return new ResponseEntity<>(
                    "Payment verified successfully",
                    HttpStatus.OK
            );
        }

        return new ResponseEntity<>(
                "Payment verification failed",
                HttpStatus.BAD_REQUEST
        );
    }

//     =========================
//     ADMIN ORDERS
//     =========================

    @GetMapping("/admin/orders")
    public ResponseEntity<OrderResponse> getAllOrders(
            @RequestParam(
                    name = "pageNumber",
                    defaultValue = AppConstants.PAGE_NUMBER,
                    required = false
            )
            Integer pageNumber,

            @RequestParam(
                    name = "pageSize",
                    defaultValue = AppConstants.PAGE_SIZE,
                    required = false
            )
            Integer pageSize,

            @RequestParam(
                    name = "sortBy",
                    defaultValue = AppConstants.SORT_ORDERS_BY,
                    required = false
            )
            String sortBy,

            @RequestParam(
                    name = "sortOrder",
                    defaultValue = AppConstants.SORT_DIR,
                    required = false
            )
            String sortOrder
    ) {

        OrderResponse orderResponse =
                orderService.getAllOrders(
                        pageNumber,
                        pageSize,
                        sortBy,
                        sortOrder
                );

        return new ResponseEntity<>(
                orderResponse,
                HttpStatus.OK
        );
    }


    // =========================
    // SELLER ORDERS
    // =========================

    @GetMapping("/seller/orders")
    public ResponseEntity<OrderResponse> getAllSellerOrders(
            @RequestParam(
                    name = "pageNumber",
                    defaultValue = AppConstants.PAGE_NUMBER,
                    required = false
            )
            Integer pageNumber,

            @RequestParam(
                    name = "pageSize",
                    defaultValue = AppConstants.PAGE_SIZE,
                    required = false
            )
            Integer pageSize,

            @RequestParam(
                    name = "sortBy",
                    defaultValue = AppConstants.SORT_ORDERS_BY,
                    required = false
            )
            String sortBy,

            @RequestParam(
                    name = "sortOrder",
                    defaultValue = AppConstants.SORT_DIR,
                    required = false
            )
            String sortOrder
    ) {

        OrderResponse orderResponse =
                orderService.getAllSellerOrders(
                        pageNumber,
                        pageSize,
                        sortBy,
                        sortOrder
                );

        return new ResponseEntity<>(
                orderResponse,
                HttpStatus.OK
        );
    }


    // =========================
    // ADMIN UPDATE ORDER
    // =========================

    @PutMapping("/admin/orders/{orderId}/status")
    public ResponseEntity<OrderDTO> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestBody OrderStatusUpdateDto orderStatusUpdateDto) {

        OrderDTO order =
                orderService.updateOrder(
                        orderId,
                        orderStatusUpdateDto.getStatus()
                );

        return new ResponseEntity<>(
                order,
                HttpStatus.OK
        );
    }


    // =========================
    // SELLER UPDATE ORDER
    // =========================

    @PutMapping("/seller/orders/{orderId}/status")
    public ResponseEntity<OrderDTO> updateOrderStatusSeller(
            @PathVariable Long orderId,
            @RequestBody OrderStatusUpdateDto orderStatusUpdateDto) {

        OrderDTO order =
                orderService.updateOrder(
                        orderId,
                        orderStatusUpdateDto.getStatus()
                );

        return new ResponseEntity<>(
                order,
                HttpStatus.OK
        );
    }

    // =========================
// USER ORDERS
// =========================

    @GetMapping("/order/users/orders")
    public ResponseEntity<OrderResponse> getUserOrders() {

        String emailId = authUtil.loggedInEmail();

        OrderResponse orderResponse =
                orderService.getUserOrders(emailId);

        return new ResponseEntity<>(
                orderResponse,
                HttpStatus.OK
        );
    }
}
