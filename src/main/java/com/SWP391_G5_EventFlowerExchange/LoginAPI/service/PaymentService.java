package com.SWP391_G5_EventFlowerExchange.LoginAPI.service;

import com.SWP391_G5_EventFlowerExchange.LoginAPI.entity.Payment;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.repository.IPaymentRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentService implements IPaymentService {

    IPaymentRepository iPaymentRepository;

    @Override
    public Payment createPayment(Payment payment) {
        payment.setMethod(payment.getMethod());
        return iPaymentRepository.save(payment);
    }

    @Override
    public List<Payment> getAllPayments() {
        return iPaymentRepository.findAll();
    }

    @Override
    public Payment getPayment(int paymentID) {
        return iPaymentRepository.findById(paymentID)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
    }

    @Override
    public Payment updatePayment(int paymentID, Payment payment) {
        Payment existingPayment = getPayment(paymentID);
        existingPayment.setMethod(payment.getMethod());
        return iPaymentRepository.save(existingPayment);
    }

    @Override
    public void deletePayment(int paymentID) {
        iPaymentRepository.deleteById(paymentID);
    }
}
