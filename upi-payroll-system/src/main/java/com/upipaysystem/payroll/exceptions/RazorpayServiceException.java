package com.upipaysystem.payroll.exceptions;

public class RazorpayServiceException extends RuntimeException{
    public RazorpayServiceException(String message){
        super(message);
    }

    public RazorpayServiceException(String message,Throwable cause){
        super(message,cause);
    }
}
