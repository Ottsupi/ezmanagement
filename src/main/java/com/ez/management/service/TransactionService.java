package com.ez.management.service;

import com.ez.management.exception.TransactionNotFoundException;
import com.ez.management.model.Transaction;
import com.ez.management.model.TransactionItem;
import com.ez.management.repository.InventoryRepository;
import com.ez.management.repository.TransactionItemRepository;
import com.ez.management.repository.TransactionRepository;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final TransactionItemRepository transactionItemRepository;
    private final InventoryRepository inventoryRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository,
                              TransactionItemRepository transactionItemRepository,
                              InventoryRepository inventoryRepository) {
        this.transactionRepository = transactionRepository;
        this.transactionItemRepository = transactionItemRepository;
        this.inventoryRepository = inventoryRepository;
    }

    public Transaction addTransaction(Transaction transaction) {
        if (transaction.getDate() == null) {
            transaction.setDate(new Date());
        }
        return transactionRepository.save(transaction);
    }

    public List<Transaction> findAllTransaction() {
        return  transactionRepository.findAll();
    }

    public Transaction findTransactionById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException(id));
    }

    public TransactionItem addTransactionItem(TransactionItem transactionItem) {
        return transactionItemRepository.save(transactionItem);
    }

    public List<TransactionItem> findAllItemsByTransactionId(Long id) {
        return transactionItemRepository.findAllByTransactionId(id);
    }

    //TODO:
    //Function that accepts a JSON file:
    //  {
    //      TransactionDetails: Transaction
    //      TransactionItems: List<Transaction>
    //  }
    public TransactionServiceOrderModel placeOrder(TransactionServiceOrderModel order) {
        Transaction transactionDetails = order.transaction();
        List<TransactionItem> transactionItems = order.transactionItemsList();

        Transaction newTransaction = addTransaction(transactionDetails);
        Long TRANSACTION_ID = newTransaction.getId();

        transactionItems.forEach(item -> item.setTransactionId(TRANSACTION_ID));
        transactionItemRepository.saveAll(transactionItems);

        TransactionServiceOrderModel newOrder = new TransactionServiceOrderModel(newTransaction, transactionItems);

        return newOrder;
    }
}
