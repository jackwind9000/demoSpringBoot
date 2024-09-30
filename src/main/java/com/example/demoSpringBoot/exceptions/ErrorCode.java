package com.example.demoSpringBoot.exceptions;

public enum ErrorCode {
    INVALID_KEY(1, "Invalid message key"),
    USER_EXISTED(1001, "User is existed"),
    USER_NOT_FOUND(1002, "User is not found"),
    USERNAME_INVALID(1003, "Username must be at least 3 characters"),
    PASSWORD_INVALID(1004, "Password must be at least 8 chars"),
    OTHER(9999, "This is an exception");

    private int code;
    private String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
