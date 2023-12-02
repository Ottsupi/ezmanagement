package com.ez.management;

import com.ez.management.model.Transaction;
import com.ez.management.model.TransactionItem;
import com.ez.management.repository.TransactionItemRepository;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.core.io.FileSystemResource;
import org.springframework.util.StringUtils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class ReceiptPrinter {


    public static InputStream printReceipts(TransactionItemRepository transactionRepository,
                                        List<Transaction> transactions,
                                        List<Long> printingOrder) throws IOException {
        Map<Long, Transaction> transactionsMap = transactions.stream()
                .collect(Collectors.toMap(Transaction::getId, item -> item));

        // File name
        Date dateCreated = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy-HHmmss");
        String fileName = formatter.format(dateCreated);


        //Generate Document
        PDDocument document = new PDDocument();
        //For each transaction
        for (Long transactionID : printingOrder) {
            Transaction transaction = transactionsMap.get(transactionID);
            List<TransactionItem> transactionItems =
                    transactionRepository.findAllByTransactionId(transaction.getId());
            printTransactionPage(document, transaction, transactionItems);
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        document.save(byteArrayOutputStream);
        document.close();

        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        return inputStream;
    }

    public static InputStream testPrint(TransactionItemRepository transactionRepository,
                                 List<Transaction> transactions,
                                 List<Long> printingOrder) throws IOException {
        Map<Long, Transaction> transactionsMap = transactions.stream()
                .collect(Collectors.toMap(Transaction::getId, item -> item));
        
        // File name
        Date dateCreated = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy-HHmmss");
        String fileName = formatter.format(dateCreated);


        //Generate Document
        PDDocument document = new PDDocument();
            //For each transaction
        for (Long transactionID : printingOrder) {
            Transaction transaction = transactionsMap.get(transactionID);
            List<TransactionItem> transactionItems =
                    transactionRepository.findAllByTransactionId(transaction.getId());
            printTransactionPage(document, transaction, transactionItems);
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        document.save(byteArrayOutputStream);
        document.close();

        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        return inputStream;
    }

    public static void printTransactionPage(PDDocument document,
                                            Transaction transaction,
                                            List<TransactionItem> transactionItems) throws IOException {

        //Sizing
        final float UNITS = 10f;
        final int WIDTH = 36;
        final float pageLength = (6 * UNITS) + (transactionItems.size() * UNITS) + .5f;
        final float pageWidth = 36 * UNITS / 2;

        //Page Settings
        PDRectangle pageSize = new PDRectangle(pageWidth, pageLength);
        PDPage page = new PDPage(pageSize);
        document.addPage(page);

        //Elements
        final Date DATE = transaction.getDate();
        SimpleDateFormat date = new SimpleDateFormat("dd/MM/yy");
        SimpleDateFormat time = new SimpleDateFormat("hh:mm a");

        String DIVIDER = "-".repeat(WIDTH);
        String TITLE = String.format("%-5s", "EZ Management");

        //Text Settings
        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        PDType1Font contentFontStyle = new PDType1Font(Standard14Fonts.FontName.COURIER);
        contentStream.setFont(contentFontStyle, 8);
        contentStream.setLeading(UNITS);


        //Setup
        contentStream.beginText();
        contentStream.newLineAtOffset(3.5f, pageLength - 9);

        //Header
        contentStream.showText(
                String.format("%-28s%s",
                        TITLE,
                        date.format(DATE))
        );
        contentStream.newLine();
        contentStream.showText(
                String.format("Invoice ID: %3s %20s",
                        transaction.getId(),
                        time.format(DATE))
        );
        contentStream.newLine();
        contentStream.showText(DIVIDER);

        //Item List
        for(TransactionItem item: transactionItems) {
            String itemName = item.getName();
            if (itemName.length() > 16) {
                itemName = itemName.substring(0, 16);
            }
            String itemLine = String.format(" %-16s %5s %11s",
                    itemName,
                    item.getQuantity(),
                    item.getTotalPrice());

            contentStream.newLine();
            contentStream.showText(itemLine);
        }

        contentStream.newLine();
        contentStream.newLine();
        contentStream.showText(
                String.format(" %22s %11s",
                        "Total Price:",
                        "P " + transaction.getTotalPrice())
        );
        contentStream.newLine();
        contentStream.showText(DIVIDER);


        contentStream.endText();
        contentStream.close();

    }
}
