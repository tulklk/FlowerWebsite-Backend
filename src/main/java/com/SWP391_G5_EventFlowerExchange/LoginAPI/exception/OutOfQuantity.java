package com.SWP391_G5_EventFlowerExchange.LoginAPI.exception;

public class OutOfQuantity extends AppException {

    public OutOfQuantity() {
        super(ErrorCode.FLOWER_OUT_OF_QUANTITY);
    }

    public OutOfQuantity(Throwable cause) {
        super(ErrorCode.FLOWER_OUT_OF_QUANTITY, cause);
    }
}
