package com.SWP391_G5_EventFlowerExchange.LoginAPI.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MonthlyRevenueResponse {
    private String month;
    private double revenue;
}
