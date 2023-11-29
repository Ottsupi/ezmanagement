package com.ez.management.exception;

public class TransactionNotFoundException extends RuntimeException {
    public TransactionNotFoundException(Long id) {
        super("Transaction with id: " + id + " not found.");
    }
}
