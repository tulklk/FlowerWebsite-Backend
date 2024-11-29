package com.SWP391_G5_EventFlowerExchange.LoginAPI.configuration;

import org.springframework.context.annotation.Configuration;

import java.util.Random;

@Configuration
public class VNPayConfig {
    public static final String vnp_TmnCode = "2FCLB0LY";
    public static final String vnp_HashSecret = "6MSE4RMSB3BF3ZV72ESVL3BJE82YFMHH";
    public static final String vnp_Url = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";  // URL VNPay sandbox
    public static final String vnp_ReturnUrl = "http://localhost:3000/success-page";  // URL trả về khi thanh toán thành công

    public static final String vnp_RefundUrl="https://sandbox.vnpayment.vn/merchant_webapi/api/transaction"; //URL VNPays sandbox refund

    public static String getRandomNumber(int len) {
        Random rnd = new Random();
        String chars = "0123456789";
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }

}
