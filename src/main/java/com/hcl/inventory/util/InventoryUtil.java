package com.hcl.inventory.util;

import com.hcl.inventory.dto.InventoryResponseDTO;
import com.hcl.inventory.entity.Inventory;
import com.hcl.inventory.filter.InventoryFilter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

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

    public static String buildNoRecordsMessage(InventoryFilter filter) {

        List<String> criteria = new ArrayList<>();

        if (filter.getName() != null) {
            criteria.add("name=" + filter.getName());
        }

        if (filter.getCategory() != null) {
            criteria.add("category=" + filter.getCategory());
        }

        if (filter.getSubCategory() != null) {
            criteria.add("subCategory=" + filter.getSubCategory());
        }

        if (filter.getSeller() != null) {
            criteria.add("seller=" + filter.getSeller());
        }

        if (filter.getLocation() != null) {
            criteria.add("location=" + filter.getLocation());
        }

        if (filter.getMinPrice() != null) {
            criteria.add("minPrice=" + filter.getMinPrice());
        }

        if (filter.getMaxPrice() != null) {
            criteria.add("maxPrice=" + filter.getMaxPrice());
        }

        return String.format(
                "No inventory records found matching filter criteria [%s]",
                String.join(", ", criteria)
        );
    }
}
