package com.SWP391_G5_EventFlowerExchange.LoginAPI.controller;

import com.SWP391_G5_EventFlowerExchange.LoginAPI.entity.Payment;
import com.SWP391_G5_EventFlowerExchange.LoginAPI.service.IPaymentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payments")
@CrossOrigin("http://localhost:3000")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentController {

    IPaymentService paymentService;

    @PostMapping("/")
    public ResponseEntity<Payment> createPayment(@RequestBody Payment payment) {
        return ResponseEntity.ok(paymentService.createPayment(payment));
    }

    @GetMapping("/")
    public ResponseEntity<List<Payment>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    @GetMapping("/{paymentID}")
    public ResponseEntity<Payment> getPayment(@PathVariable int paymentID) {
        return ResponseEntity.ok(paymentService.getPayment(paymentID));
    }

    @PutMapping("/{paymentID}")
    public ResponseEntity<Payment> updatePayment(@PathVariable int paymentID, @RequestBody Payment payment) {
        return ResponseEntity.ok(paymentService.updatePayment(paymentID, payment));
    }

    @DeleteMapping("/{paymentID}")
    public ResponseEntity<String> deletePayment(@PathVariable int paymentID) {
        paymentService.deletePayment(paymentID);
        return ResponseEntity.ok("Payment deleted!");
    }
}
