import React from "react";
import { useRazorpay } from "react-razorpay";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";

import {
  createRazorpayOrder,
  verifyRazorpayPayment,
  razorpayPaymentConfirmation,
} from "../../store/actions";

const RazorpayPayment = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const { Razorpay, isLoading, error } = useRazorpay();

  const { totalPrice } = useSelector((state) => state.carts);

  const { user, selectedUserCheckoutAddress } = useSelector(
    (state) => state.auth
  );

  const handlePayment = async () => {
    // 1. Create Razorpay Order
    const sendData = {
      amount: Number(totalPrice) * 100,
      currency: "INR",
      receipt: `receipt_${Date.now()}`,
    };

    const data = await dispatch(createRazorpayOrder(sendData));

    if (!data) {
      return;
    }

    console.log("Razorpay Order Response:", data);

    // 2. Razorpay Checkout Options
    const options = {
      key: import.meta.env.VITE_RAZORPAY_KEY_ID,

      amount: data.amount,
      currency: data.currency,

      name: "E-Commerce",
      description: "E-Commerce Order",

      order_id: data.orderId,

      prefill: {
        name: user?.username,
        email: user?.email,
      },

      theme: {
        color: "#3399cc",
      },

      // 3. Payment Success
      handler: async function (response) {
        console.log("Razorpay Payment Response:", response);

        // 4. Verify Payment
        const verifyData = {
          razorpayOrderId: response.razorpay_order_id,
          razorpayPaymentId: response.razorpay_payment_id,
          razorpaySignature: response.razorpay_signature,
          addressId: selectedUserCheckoutAddress.addressId,
        };

        const verified = await dispatch(
          verifyRazorpayPayment(verifyData)
        );

        if (!verified) {
          console.log("Payment verification failed");
          return;
        }

        console.log("Payment verified successfully");

        // 5. Save Order
        const orderData = {
          addressId: selectedUserCheckoutAddress.addressId,
          paymentMethod: "Razorpay",
          pgName: "Razorpay",
          pgPaymentId: response.razorpay_payment_id,
          pgStatus: "succeeded",
          pgResponseMessage: "Payment verified successfully",
        };

        const order = await dispatch(
          razorpayPaymentConfirmation(orderData)
        );

        // 6. Order Successfully Created
        if (order) {
          console.log("Order placed successfully:", order);

          // Send complete order data to confirmation page
          navigate("/order-confirm", {
            state: {
              order: order,
            },
          });
        } else {
          console.log("Order placement failed");
        }
      },
    };

    // 7. Open Razorpay
    const razorpay = new Razorpay(options);

    razorpay.open();
  };

  if (error) {
    console.log("Razorpay Error:", error);
  }

  return (
    <div className="max-w-lg mx-auto p-4">
      <button
        onClick={handlePayment}
        disabled={isLoading}
        className="text-white w-full px-5 py-[10px] bg-black mt-2 rounded-md font-bold disabled:opacity-50"
      >
        {isLoading
          ? "Processing..."
          : `Pay ₹${Number(totalPrice).toFixed(2)}`}
      </button>
    </div>
  );
};

export default RazorpayPayment;
