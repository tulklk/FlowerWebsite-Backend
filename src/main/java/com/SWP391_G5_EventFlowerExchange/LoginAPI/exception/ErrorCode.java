package com.SWP391_G5_EventFlowerExchange.LoginAPI.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    // Advance Exception Handling Class
    // Define Error here:
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized Exception.", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY_EXCEPTION(9999, "Invalid Error Message Key!", HttpStatus.BAD_REQUEST),
    EMAIL_EXISTED(1001, "Email is already existed!", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1002, "User is invalid!", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1003, "Password must be at least 5 characters!", HttpStatus.BAD_REQUEST),
    DELETE_USER_ERROR(1004, "Cannot delete this user!", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005, "User is not existed!", HttpStatus.NOT_FOUND),
    USERID_NOT_FOUND(1006, "UserID is not found!", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1007, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1008, "You do not have permission", HttpStatus.FORBIDDEN),
    USER_NOT_AVAILABLE(1009, "User is not available for login", HttpStatus.FORBIDDEN),
    USER_NOT_VERIFIED(10010, "User is not verified", HttpStatus.FORBIDDEN),
    FLOWER_OUT_OF_QUANTITY(1011, "Flower Out of Quantity", HttpStatus.BAD_REQUEST),;
    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode= statusCode;
    }
}
