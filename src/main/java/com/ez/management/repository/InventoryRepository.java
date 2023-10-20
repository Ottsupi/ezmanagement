package com.ez.management.repository;

import com.ez.management.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    @Transactional
    void deleteInventoryById(Long id);

    Optional<Inventory> findInventoryById(Long id);
}
