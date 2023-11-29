package com.ez.management.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
public class Transaction implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;
    @Column(nullable = false)
    private Date date;
    @Column(nullable = false)
    private BigDecimal totalPrice;
    @Column(nullable = false)
    private Integer totalQuantity;
    @Column(nullable = false)
    private Integer totalItems;

    public Transaction() {
    }

    public Transaction(Long id, Date date, BigDecimal totalPrice, Integer totalQuantity, Integer totalItems) {
        this.id = id;
        this.date = date;
        this.totalPrice = totalPrice;
        this.totalQuantity = totalQuantity;
        this.totalItems = totalItems;
    }

    public Transaction(Date date, BigDecimal totalPrice, Integer totalQuantity, Integer totalItems) {
        this.date = date;
        this.totalPrice = totalPrice;
        this.totalQuantity = totalQuantity;
        this.totalItems = totalItems;
    }

    public Long getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public Integer getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(Integer totalItems) {
        this.totalItems = totalItems;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", date=" + date +
                ", totalPrice=" + totalPrice +
                ", totalQuantity=" + totalQuantity +
                ", totalItems=" + totalItems +
                '}';
    }
}
