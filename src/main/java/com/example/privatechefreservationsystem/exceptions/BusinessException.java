package com.example.privatechefreservationsystem.exceptions;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final String rtnCode;
    private final String messageKey;

    public BusinessException(String rtnCode, String messageKey) {
        super(messageKey);
        this.rtnCode = rtnCode;
        this.messageKey = messageKey;
    }
}
