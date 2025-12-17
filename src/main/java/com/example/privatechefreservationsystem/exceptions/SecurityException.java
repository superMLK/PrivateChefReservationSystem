package com.example.privatechefreservationsystem.exceptions;
import lombok.Getter;
/**
 * 安全相關自訂異常
 */
@Getter
public class SecurityException extends RuntimeException {
    private final String errorCode;
    private final String errorMessage;
    public SecurityException(String errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
    public SecurityException(String errorCode, String errorMessage, Throwable cause) {
        super(errorMessage, cause);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
