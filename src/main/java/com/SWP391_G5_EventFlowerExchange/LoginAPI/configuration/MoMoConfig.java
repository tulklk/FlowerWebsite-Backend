package com.SWP391_G5_EventFlowerExchange.LoginAPI.configuration;

import org.springframework.context.annotation.Configuration;
import java.util.Random;

@Configuration
public class MoMoConfig {
    public static final String MOMO_URL = "https://test-payment.momo.vn/pay/store/MOMO";  // MoMo Sandbox URL
    public static final String PARTNER_CODE = "YOUR_PARTNER_CODE";
    public static final String ACCESS_KEY = "YOUR_ACCESS_KEY";
    public static final String SECRET_KEY = "YOUR_SECRET_KEY";
    public static final String RETURN_URL = "http://localhost:3000/momo-success";  // URL returned after successful payment
    public static final String NOTIFY_URL = "http://localhost:3000/momo-notify";   // URL to receive payment notifications

    // Generates a random transaction request ID
    public static String getRandomRequestId(int length) {
        Random random = new Random();
        String characters = "0123456789";
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }
        return sb.toString();
    }
}
