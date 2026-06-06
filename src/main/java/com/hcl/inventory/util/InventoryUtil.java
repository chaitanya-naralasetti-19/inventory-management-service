package com.hcl.inventory.util;

import com.hcl.inventory.dto.InventoryResponseDTO;
import com.hcl.inventory.entity.Inventory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InventoryUtil {
    private InventoryUtil() {}

    public static InventoryResponseDTO convertToDto(Inventory inventory) {
        log.info("Converting inventory entity to DTO: {}", inventory);
        return InventoryResponseDTO.builder()
                .id(inventory.getId())
                .name(inventory.getName())
                .category(inventory.getCategory())
                .subCategory(inventory.getSubCategory())
                .model(inventory.getModel())
                .specification(inventory.getSpecification())
                .seller(inventory.getSeller())
                .location(inventory.getLocation())
                .price(inventory.getPrice())
                .stock(inventory.getStock())
                .manufacturingDate(inventory.getManufacturingDate())
                .expiryDate(inventory.getExpiryDate())
                .createdAt(inventory.getCreatedAt())
                .updatedAt(inventory.getUpdatedAt())
                .build();
    }
}
