import React, { useEffect, useState } from "react";
import { FaCheckCircle } from "react-icons/fa";
import { useDispatch } from "react-redux";
import { useLocation, useNavigate } from "react-router-dom";
import { stripePaymentConfirmation } from "../../store/actions";
import toast from "react-hot-toast";

const PaymentConfirmation = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const dispatch = useDispatch();

  const [errorMessage, setErrorMessage] = useState("");
  const [loading, setLoading] = useState(false);

  // Razorpay / common order data
  const order = location.state?.order;

  // Stripe URL data
  const searchParams = new URLSearchParams(location.search);

  const paymentIntent = searchParams.get("payment_intent");
  const clientSecret = searchParams.get(
    "payment_intent_client_secret"
  );
  const redirectStatus = searchParams.get("redirect_status");

  /*
   * =========================
   * STRIPE FLOW
   * =========================
   */
  useEffect(() => {

    // Agar Razorpay order already mil gaya hai,
    // Stripe wala code run nahi karna
    if (order) {
      return;
    }

    const selectedUserCheckoutAddress =
      localStorage.getItem("CHECKOUT_ADDRESS")
        ? JSON.parse(
            localStorage.getItem("CHECKOUT_ADDRESS")
          )
        : [];

    if (
      paymentIntent &&
      clientSecret &&
      redirectStatus
    ) {
      const sendData = {
        addressId:
          selectedUserCheckoutAddress.addressId,

        pgName: "Stripe",

        pgPaymentId: paymentIntent,

        pgStatus: "succeeded",

        pgResponseMessage: "Payment successful",
      };

      console.log("Stripe Payment Data:", sendData);

      dispatch(
        stripePaymentConfirmation(
          sendData,
          setErrorMessage,
          setLoading,
          toast
        )
      );
    }

  }, [
    paymentIntent,
    clientSecret,
    redirectStatus,
    order,
    dispatch,
  ]);

  /*
   * =========================
   * RAZORPAY / COMMON ORDER
   * =========================
   */

  if (order) {
    return (
      <div className="min-h-screen bg-gray-50 py-10 px-4">

        <div className="max-w-4xl mx-auto">

          {/* Success */}
          <div className="bg-white rounded-lg shadow-md p-8 text-center">

            <div className="text-green-500 flex justify-center mb-4">
              <FaCheckCircle size={64} />
            </div>

            <h2 className="text-3xl font-bold text-gray-800">
              Order Confirmed!
            </h2>

            <p className="text-gray-600 mt-2">
              Thank you for your purchase.
              Your order has been successfully placed.
            </p>

          </div>

          {/* Order Information */}
          <div className="bg-white rounded-lg shadow-md p-6 mt-6">

            <h3 className="text-xl font-bold mb-5">
              Order Information
            </h3>

            <div className="grid grid-cols-1 md:grid-cols-3 gap-5">

              <div>
                <p className="text-gray-500 text-sm">
                  Order ID
                </p>

                <p className="font-semibold">
                  #{order.orderId}
                </p>
              </div>

              <div>
                <p className="text-gray-500 text-sm">
                  Order Date
                </p>

                <p className="font-semibold">
                  {order.orderDate}
                </p>
              </div>

              <div>
                <p className="text-gray-500 text-sm">
                  Order Status
                </p>

                <p className="font-semibold text-green-600">
                  {order.orderStatus}
                </p>
              </div>

            </div>

          </div>

          {/* Products */}
          <div className="bg-white rounded-lg shadow-md p-6 mt-6">

            <h3 className="text-xl font-bold mb-5">
              Ordered Products
            </h3>

            <div className="space-y-5">

              {order.orderItems?.map((item) => (

                <div
                  key={item.orderItemId}
                  className="flex gap-4 border-b pb-5"
                >

                  <img
                    src={item.product?.image}
                    alt={item.product?.productName}
                    className="w-24 h-24 object-contain border rounded-md"
                  />

                  <div className="flex-1">

                    <h4 className="font-semibold text-lg">
                      {item.product?.productName}
                    </h4>

                    <p className="text-gray-600">
                      Quantity: {item.quantity}
                    </p>

                    <p className="text-gray-600">
                      Price: ₹{item.orderedProductPrice}
                    </p>

                    <p className="text-gray-600">
                      Discount: {item.discount}%
                    </p>

                  </div>

                  <div className="font-semibold">
                    ₹
                    {(
                      item.orderedProductPrice *
                      item.quantity
                    ).toFixed(2)}
                  </div>

                </div>

              ))}

            </div>

            <div className="border-t mt-6 pt-5 flex justify-between">

              <span className="text-xl font-bold">
                Total Amount
              </span>

              <span className="text-xl font-bold">
                ₹
                {Number(order.totalAmount).toFixed(2)}
              </span>

            </div>

          </div>

          {/* Payment Information */}
          <div className="bg-white rounded-lg shadow-md p-6 mt-6">

            <h3 className="text-xl font-bold mb-4">
              Payment Information
            </h3>

            <div className="space-y-2">

              <p>
                <span className="font-semibold">
                  Payment Method:
                </span>{" "}
                {order.payment?.paymentMethod}
              </p>

              <p>
                <span className="font-semibold">
                  Payment Gateway:
                </span>{" "}
                {order.payment?.pgName}
              </p>

              <p>
                <span className="font-semibold">
                  Payment ID:
                </span>{" "}
                {order.payment?.pgPaymentId}
              </p>

              <p>
                <span className="font-semibold">
                  Payment Status:
                </span>{" "}
                <span className="text-green-600">
                  {order.payment?.pgStatus}
                </span>
              </p>

            </div>

          </div>

          <div className="text-center mt-8">

            <button
              onClick={() => navigate("/")}
              className="bg-black text-white px-6 py-3 rounded-md font-semibold"
            >
              Continue Shopping
            </button>

          </div>

        </div>
      </div>
    );
  }

  /*
   * =========================
   * STRIPE SUCCESS UI
   * =========================
   */

  return (
    <div className="min-h-screen flex items-center justify-center">

      {loading ? (

        <div>
          Processing payment...
        </div>

      ) : (

        <div className="p-8 rounded-lg shadow-lg text-center max-w-md mx-auto border border-gray-200">

          <div className="text-green-500 mb-4 flex justify-center">
            <FaCheckCircle size={64} />
          </div>

          <h2 className="text-3xl font-bold text-gray-800 mb-2">
            Payment Successful!
          </h2>

          <p className="text-gray-600 mb-6">
            Thank you for your purchase! Your payment was
            successful, and we are processing your order.
          </p>

          {errorMessage && (
            <p className="text-red-500">
              {errorMessage}
            </p>
          )}

        </div>

      )}

    </div>
  );
};

export default PaymentConfirmation;