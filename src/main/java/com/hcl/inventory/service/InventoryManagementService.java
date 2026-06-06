package com.hcl.inventory.service;

import com.hcl.inventory.dto.InventoryResponseDTO;
import com.hcl.inventory.filter.InventoryFilter;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;

public interface InventoryManagementService {
    Page<InventoryResponseDTO> searchInventory(InventoryFilter filter, Pageable pageable);
}
