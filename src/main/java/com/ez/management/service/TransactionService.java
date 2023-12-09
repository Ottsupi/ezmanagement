package com.ez.management.service;

import com.ez.management.ReceiptPrinter;
import com.ez.management.exception.TransactionNotFoundException;
import com.ez.management.model.Inventory;
import com.ez.management.model.Transaction;
import com.ez.management.model.TransactionItem;
import com.ez.management.repository.InventoryRepository;
import com.ez.management.repository.TransactionItemRepository;
import com.ez.management.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final TransactionItemRepository transactionItemRepository;
    private final InventoryRepository inventoryRepository;

    private static ReceiptPrinter printer;

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
        return  transactionRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    public Transaction findTransactionById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException(id));
    }

    public TransactionItem addTransactionItem(TransactionItem transactionItem) {
        return transactionItemRepository.save(transactionItem);
    }

    public void deleteTransactionById(Long id) {
        transactionRepository.deleteById(id);
    }

    public List<TransactionItem> findAllItemsByTransactionId(Long id) {
        return transactionItemRepository.findAllByTransactionId(id);
    }

    //TODO:
    //update inventory stock on change
    public boolean placeOrder(TransactionServiceOrderModel order) {
        Transaction transactionDetails = order.transaction();
        List<TransactionItem> transactionItems = order.transactionItemsList();

        Transaction newTransaction = addTransaction(transactionDetails);
        Long TRANSACTION_ID = newTransaction.getId();

        transactionItems.forEach(item -> item.setTransactionId(TRANSACTION_ID));
        List<TransactionItem> changes = transactionItemRepository.saveAll(transactionItems);

        updateInventoryStockOnOrder(transactionItems);
        TransactionServiceOrderModel newOrder = new TransactionServiceOrderModel(newTransaction, transactionItems);

        if (changes.isEmpty()) {
            //TODO: Throw error if there were no changes.
            return false;
        }
        return true;

    }

    public void updateInventoryStockOnOrder(List<TransactionItem> transactionItems) {

        Map<Long, TransactionItem> transactionItemMap = transactionItems.stream()
                .collect(Collectors.toMap(TransactionItem::getInventoryId, item -> item));
        List<Long> ORDER_IDS = new ArrayList<>(transactionItemMap.keySet());

        List<Inventory> inventory = inventoryRepository.findAllById(ORDER_IDS);

        inventory.forEach(item -> {
            Integer STOCK_QUANTITY = item.getQuantity();
            Integer ORDER_QUANTITY = transactionItemMap.get(item.getId()).getQuantity();
            item.setQuantity(STOCK_QUANTITY - ORDER_QUANTITY);
        });

        inventoryRepository.saveAll(inventory);
    }

    public InputStream requestPrintReceipts(List<Long> printList) throws IOException {
        List<Transaction> transactionsToBePrinted = transactionRepository.findAllById(printList);

        InputStream document = ReceiptPrinter.printReceipts(transactionItemRepository, transactionsToBePrinted, printList);

        return document;
    }

    public InputStream testPrint() throws IOException {
        List<Long> printList = new ArrayList<>(Arrays.asList(4L, 5L, 2L));
        List<Transaction> transactionsToBePrinted = transactionRepository.findAllById(printList);

        return ReceiptPrinter.testPrint(transactionItemRepository, transactionsToBePrinted, printList);
    }


}
