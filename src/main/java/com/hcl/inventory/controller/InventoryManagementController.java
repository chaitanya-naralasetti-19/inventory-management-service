package com.hcl.inventory.controller;

import com.hcl.inventory.dto.InventoryResponseDTO;
import com.hcl.inventory.filter.InventoryFilter;
import com.hcl.inventory.service.InventoryManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/inventory")
@Slf4j
public class InventoryManagementController {

    private final InventoryManagementService inventoryManagementService;

    @GetMapping("/search-inventory")
    public Page<InventoryResponseDTO> searchInventory(InventoryFilter inventoryFilter, Pageable pageable) {
        log.info("Received search inventory request with filter and pageable: {} :: {}", inventoryFilter, pageable);
        return inventoryManagementService.searchInventory(inventoryFilter, pageable);
    }

}
