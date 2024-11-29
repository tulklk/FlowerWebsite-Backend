package com.SWP391_G5_EventFlowerExchange.LoginAPI.service;

import com.SWP391_G5_EventFlowerExchange.LoginAPI.dto.response.MomoResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class MomoPaymentService {

    private static final String MOMO_API_ENDPOINT = "https://test-payment.momo.vn/v2/gateway/api/create";

    public MomoResponse createMomoPayment(BigDecimal totalAmount, String orderId, String returnUrl) {
        RestTemplate restTemplate = new RestTemplate();

        // Prepare the request body to send to MoMo
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("partnerCode", "yourPartnerCode");
        requestBody.put("accessKey", "yourAccessKey");
        requestBody.put("secretKey", "yourSecretKey");
        requestBody.put("requestId", orderId);
        requestBody.put("amount", totalAmount.toString());
        requestBody.put("orderId", orderId);
        requestBody.put("orderInfo", "Flower Order #" + orderId);
        requestBody.put("redirectUrl", returnUrl);
        requestBody.put("ipnUrl", "http://localhost:8080/api/payment/ipn");

        // Call the MoMo API to create a payment
        MomoResponse response = restTemplate.postForObject(MOMO_API_ENDPOINT, requestBody, MomoResponse.class);

        return response;
    }
}
