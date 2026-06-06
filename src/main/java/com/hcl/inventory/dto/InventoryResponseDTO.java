package com.hcl.inventory.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class InventoryResponseDTO {
    private Long id;
    private String name;
    private String category;
    private String subCategory;
    private String model;
    private String specification;
    private String seller;
    private String location;
    private BigDecimal price;
    private Integer stock;
    private LocalDate manufacturingDate;
    private LocalDate expiryDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}