package com.ez.management.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
public class TransactionItem implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;
    @Column(nullable = false)
    private Long transactionId;
    @Column(nullable = false)
    private Long inventoryId;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private BigDecimal price;
    @Column(nullable = false)
    private Integer quantity;
    @Column(nullable = false)
    private BigDecimal totalPrice;

    public TransactionItem() {
    }

    public TransactionItem(Long id,
                           Long transactionId,
                           Long inventoryId,
                           String name,
                           BigDecimal price,
                           Integer quantity,
                           BigDecimal totalPrice) {
        this.id = id;
        this.transactionId = transactionId;
        this.inventoryId = inventoryId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public TransactionItem(Long transactionId,
                           Long inventoryId,
                           String name,
                           BigDecimal price,
                           Integer quantity,
                           BigDecimal totalPrice) {
        this.transactionId = transactionId;
        this.inventoryId = inventoryId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public TransactionItem(Long inventoryId,
                           String name,
                           BigDecimal price,
                           Integer quantity,
                           BigDecimal totalPrice) {
        this.inventoryId = inventoryId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public Long getId() {
        return id;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public Long getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(Long inventoryId) {
        this.inventoryId = inventoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return "TransactionItem{" +
                "id=" + id +
                ", transactionId=" + transactionId +
                ", inventoryId=" + inventoryId +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
