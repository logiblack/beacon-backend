package com.lagranmoon.beacon.exception;

import lombok.Getter;

/**
 * @author Lagranmoon
 */
public class UnAuthenticationException extends RuntimeException {

    @Getter
    private Integer errCode;

    public UnAuthenticationException(String message) {
        super(message);
    }

    public UnAuthenticationException(String message, Integer errCode) {
        super(message);
        this.errCode = errCode;
    }
}
