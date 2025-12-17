package com.example.privatechefreservationsystem.exceptions;

import com.example.privatechefreservationsystem.dtos.ApiResponse;
import com.example.privatechefreservationsystem.dtos.ApiResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.lang.reflect.InvocationTargetException;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(
            BusinessException ex) {
        return ResponseEntity.badRequest().body(
                ApiResponse.failure(
                        ex.getRtnCode(),
                        ex.getMessageKey()
                )
        );
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<ApiResponse<Void>> handleSecurityException(
            SecurityException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                ApiResponse.failure(
                        ex.getErrorCode(),
                        ex.getErrorMessage()
                )
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(
            MethodArgumentNotValidException ex) {
        // ex.getBindingResult().getFieldErrors() 會包含像是 "email 不能為空", "age 必須大於 18" 等資訊
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage()) // 格式範例："guestName: 不能為空"
                .collect(Collectors.joining(", ")); // 把多個錯誤串起來

        // 2. 回傳 400 Bad Request，並把詳細原因放在 message 裡
        return ResponseEntity.badRequest().body(
                ApiResponse.failure(
                        ApiResponseCode.PARAM_INVALID, // 假設你有定義一個「參數錯誤」的代碼 (例如 "ERR_002")
                        "參數錯誤: " + errorMessage
                )
        );
    }

    @ExceptionHandler(InvocationTargetException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvocationTargetException(
            InvocationTargetException ex) {
        // InvocationTargetException 是包裝異常，需要提取真正的原因
        Throwable targetException = ex.getTargetException();

        // 如果內部異常是 BusinessException，則按 BusinessException 處理
        if (targetException instanceof BusinessException) {
            return handleBusinessException((BusinessException) targetException);
        }

        // 如果內部異常是 SecurityException，則按 SecurityException 處理
        if (targetException instanceof SecurityException) {
            return handleSecurityException((SecurityException) targetException);
        }

        // 否則作為一般異常處理
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ApiResponse.failure(
                        ApiResponseCode.SYSTEM_ERROR,
                        "系統忙碌中，請稍後再試: " + targetException.getMessage()
                )
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception ex) {
        // 記錄完整的異常堆疊信息以便調試
        log.error("捕捉到未處理的異常: 類型={}, 訊息={}", ex.getClass().getName(), ex.getMessage(), ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ApiResponse.failure(
                        ApiResponseCode.SYSTEM_ERROR,
                        "系統忙碌中，請稍後再試"
                )
        );
    }
}
