package com.ez.management;

import com.ez.management.model.Inventory;
import com.ez.management.model.Transaction;
import com.ez.management.model.TransactionItem;
import com.ez.management.service.TransactionService;
import com.ez.management.service.TransactionServiceOrderModel;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("api/transaction")
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

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteItemById(@PathVariable("id") Long id) {
        transactionService.deleteTransactionById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/find/{id}/items")
    public ResponseEntity<List<TransactionItem>> findTransactionItemsById(@PathVariable("id") Long id) {
        List<TransactionItem> itemList = transactionService.findAllItemsByTransactionId(id);
        return new ResponseEntity<>(itemList, HttpStatus.OK);
    }

    @PostMapping("/order")
    public ResponseEntity<Boolean> placeOrder(@RequestBody TransactionServiceOrderModel order) {
        boolean res = transactionService.placeOrder(order);
        return new ResponseEntity<Boolean>(res, HttpStatus.OK);
    }

    @PostMapping("/print")
    public void checkInventory(HttpServletResponse response, @RequestBody List<Long> printList) throws IOException {

        try (InputStream in = transactionService.requestPrintReceipts(printList);
             OutputStream out = response.getOutputStream()) {
            Date dateCreated = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy-HHmmss");
            String fileName = formatter.format(dateCreated);

            // Set the content type based on your file type
            response.setContentType("application/pdf");

            // Set the headers
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            response.setHeader("Content-Length", String.valueOf(in.available()));
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");

            // Copy the file content to the response output stream
            FileCopyUtils.copy(in, out);

            // Flush the buffer to send the response
            response.flushBuffer();
        } catch (IOException e) {
            // Handle exception (log it, throw it, etc.)
            e.printStackTrace();
        }
    }

    @GetMapping("/getWorld")
    public void getWorld(HttpServletResponse response) throws IOException {
        FileSystemResource resource = new FileSystemResource("print/pdfBoxHelloWorld.pdf");
        File file = resource.getFile();

        try (InputStream in = new FileInputStream(file);
             OutputStream out = response.getOutputStream()) {

            // Set the content type based on your file type
            response.setContentType("text/csv");

            // Set the headers
            response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());
            response.setHeader("Content-Length", String.valueOf(file.length()));

            // Copy the file content to the response output stream
            FileCopyUtils.copy(in, out);

            // Flush the buffer to send the response
            response.flushBuffer();
        } catch (IOException e) {
            // Handle exception (log it, throw it, etc.)
            e.printStackTrace();
        }
    }

    @GetMapping("/testPrint")
    public void testPrint(HttpServletResponse response) throws IOException {

        try (InputStream in = transactionService.testPrint();
             OutputStream out = response.getOutputStream()) {
            Date dateCreated = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy-HHmmss");
            String fileName = formatter.format(dateCreated);

            // Set the content type based on your file type
            response.setContentType("application/pdf");

            // Set the headers
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            response.setHeader("Content-Length", String.valueOf(in.available()));
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");

            // Copy the file content to the response output stream
            FileCopyUtils.copy(in, out);

            // Flush the buffer to send the response
            response.flushBuffer();
        } catch (IOException e) {
            // Handle exception (log it, throw it, etc.)
            e.printStackTrace();
        }
    }
}
