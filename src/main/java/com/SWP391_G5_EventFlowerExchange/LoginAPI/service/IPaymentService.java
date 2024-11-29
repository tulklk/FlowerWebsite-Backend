package com.SWP391_G5_EventFlowerExchange.LoginAPI.service;

import com.SWP391_G5_EventFlowerExchange.LoginAPI.entity.Payment;

import java.util.List;

public interface IPaymentService {
    Payment createPayment(Payment payment);
    List<Payment> getAllPayments();
    Payment getPayment(int paymentID);
    Payment updatePayment(int paymentID, Payment payment);
    void deletePayment(int paymentID);
}
