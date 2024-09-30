package com.example.demoSpringBoot.exceptions;

import com.example.demoSpringBoot.dto.request.ApiReponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiReponse> handleException(Exception ex) {
        ApiReponse apiReponse = new ApiReponse();
        apiReponse.setCode(ErrorCode.OTHER.getCode());
        apiReponse.setMessage(ErrorCode.OTHER.getMessage());
        return ResponseEntity.badRequest().body(apiReponse);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiReponse> handleAppException(AppException ex) {
        ApiReponse apiReponse = new ApiReponse();
        ErrorCode errCode = ex.getErrorCode();
        apiReponse.setCode(errCode.getCode());
        apiReponse.setMessage(errCode.getMessage());
        return ResponseEntity.badRequest().body(apiReponse);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiReponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        ApiReponse apiReponse = new ApiReponse();
        ErrorCode errCode = ErrorCode.INVALID_KEY;
        String key = Objects.requireNonNull(ex.getFieldError()).getDefaultMessage();

        try {
            errCode = ErrorCode.valueOf(key);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid key: " + e.getMessage());
        }


        apiReponse.setCode(errCode.getCode());
        apiReponse.setMessage(errCode.getMessage());
        return ResponseEntity.badRequest().body(apiReponse);
    }

}
