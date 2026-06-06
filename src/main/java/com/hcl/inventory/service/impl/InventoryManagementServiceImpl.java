package com.hcl.inventory.service.impl;

import com.hcl.inventory.dto.InventoryResponseDTO;
import com.hcl.inventory.entity.Inventory;
import com.hcl.inventory.filter.InventoryFilter;
import com.hcl.inventory.repository.InventoryRepository;
import com.hcl.inventory.service.InventoryManagementService;
import com.hcl.inventory.specification.InventorySpecification;
import com.hcl.inventory.util.InventoryUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
@Service
@Slf4j
public class InventoryManagementServiceImpl implements InventoryManagementService {
    private final InventoryRepository inventoryRepository;
    @Override
    public Page<InventoryResponseDTO> searchInventory(InventoryFilter filter, Pageable pageable) {
        log.info("Searching inventory with filter: {} and pageable: {}", filter, pageable);
        Specification<Inventory> specification = InventorySpecification.build(filter);
        Page<Inventory> inventoryPage = inventoryRepository.findAll(specification, pageable);
        log.info("Found {} inventory items", inventoryPage.getTotalElements());
        return inventoryPage.map(InventoryUtil::convertToDto);
    }
}
