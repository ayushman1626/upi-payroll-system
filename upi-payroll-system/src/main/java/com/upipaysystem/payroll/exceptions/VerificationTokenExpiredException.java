package com.upipaysystem.payroll.exceptions;

public class VerificationTokenExpiredException extends RuntimeException {
    public VerificationTokenExpiredException(String message){
        super(message);
    }
}
