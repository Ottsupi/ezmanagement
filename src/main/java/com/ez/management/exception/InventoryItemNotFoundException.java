package com.ez.management.exception;

public class InventoryItemNotFoundException extends RuntimeException {
    public InventoryItemNotFoundException(Long id) {
        super("Item with id: " + id + " not found.");
    }
}
