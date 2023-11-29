package com.ez.management;

import com.ez.management.model.Inventory;
import com.ez.management.model.Transaction;
import com.ez.management.model.TransactionItem;
import com.ez.management.service.TransactionService;
import com.ez.management.service.TransactionServiceOrderModel;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transaction")
public class TransactionResource {
    private final TransactionService transactionService;

    public TransactionResource(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Transaction>> getAllTransaction() {
        List<Transaction> allTransactions = transactionService.findAllTransaction();
        return new ResponseEntity<>(allTransactions, HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<Transaction> findTransactionById(@PathVariable("id") Long id) {
        Transaction transaction = transactionService.findTransactionById(id);
        return new ResponseEntity<>(transaction, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Transaction> addInventory(@RequestBody Transaction transaction) {
        Transaction newTransaction = transactionService.addTransaction(transaction);
        return new ResponseEntity<>(newTransaction, HttpStatus.CREATED);
    }

    @PostMapping("/add/item")
    public ResponseEntity<TransactionItem> addInventory(@RequestBody TransactionItem transactionItem) {
        TransactionItem newTransactionItem = transactionService.addTransactionItem(transactionItem);
        return new ResponseEntity<>(newTransactionItem, HttpStatus.CREATED);
    }

    @GetMapping("/find/{id}/items")
    public ResponseEntity<List<TransactionItem>> findTransactionItemsById(@PathVariable("id") Long id) {
        List<TransactionItem> itemList = transactionService.findAllItemsByTransactionId(id);
        return new ResponseEntity<>(itemList, HttpStatus.OK);
    }

    @PostMapping("/order")
    public ResponseEntity<String> placeOrder(@RequestBody TransactionServiceOrderModel order) {
        TransactionServiceOrderModel res = transactionService.placeOrder(order);
        return new ResponseEntity<>(res.toString(), HttpStatus.OK);
    }
}
