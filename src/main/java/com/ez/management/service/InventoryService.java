package com.ez.management.service;

import com.ez.management.exception.InventoryItemNotFoundException;
import com.ez.management.model.Inventory;
import com.ez.management.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryService {
    private final InventoryRepository inventoryRepository;

    @Autowired
    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public Inventory addInventory(Inventory inventory) {
        return inventoryRepository.save(inventory);
    }

    public List<Inventory> findAllInventory() {
        return inventoryRepository.findAll();
    }

    public Inventory updateInventory(Inventory inventory) {
        return inventoryRepository.save(inventory);
    }

    public void deleteInventory(Long id) {
        inventoryRepository.deleteInventoryById(id);
    }

    public Inventory findInventoryById(Long id) {
        return inventoryRepository.findInventoryById(id)
                .orElseThrow(() -> new InventoryItemNotFoundException(id));
    }
}
