package com.upipaysystem.payroll.exceptions;

public class OrganizationExistException extends RuntimeException{
    public OrganizationExistException(String message) {
        super(message);
    }
}
