package com.hcl.inventory.filter;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class InventoryFilter {

    private Long id;

    private String name;

    private String category;

    private String subCategory;

    private String model;

    private String specification;

    private String seller;

    private String location;

    private BigDecimal minPrice;
    private BigDecimal maxPrice;

    private Integer minStock;
    private Integer maxStock;

    private LocalDate manufacturingDateFrom;
    private LocalDate manufacturingDateTo;

    private LocalDate expiryDateFrom;
    private LocalDate expiryDateTo;
}