package com.ez.management.service;

import com.ez.management.model.Transaction;
import com.ez.management.model.TransactionItem;

import java.util.List;

public record TransactionServiceOrderModel(Transaction transaction, List<TransactionItem> transactionItemsList) {
}
