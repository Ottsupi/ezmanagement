package com.ez.management.repository;

import com.ez.management.model.TransactionItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionItemRepository extends JpaRepository<TransactionItem, Long> {
    List<TransactionItem> findAllByTransactionId(Long id);
}
