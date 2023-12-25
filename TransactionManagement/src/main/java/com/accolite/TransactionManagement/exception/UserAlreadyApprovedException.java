package com.example.TransactionManagement.exceptions;

public class UserAlreadyApprovedException extends RuntimeException {
    public UserAlreadyApprovedException(String message) {super(message);}
}
