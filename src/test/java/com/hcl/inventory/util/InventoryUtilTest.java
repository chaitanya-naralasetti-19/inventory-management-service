package com.hcl.inventory.util;

import com.hcl.inventory.dto.InventoryResponseDTO;
import com.hcl.inventory.entity.Inventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.hcl.inventory.filter.InventoryFilter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class InventoryUtilTest {

    private Inventory inventory;
    private LocalDateTime testDateTime;

    @BeforeEach
    void setUp() {
        LocalDate testDate = LocalDate.of(2024, 6, 6);
        testDateTime = LocalDateTime.of(2024, 6, 6, 10, 30, 0);

        inventory = Inventory.builder()
                .id(1L)
                .name("Test Product")
                .category("Electronics")
                .subCategory("Mobile Devices")
                .model("Model X")
                .specification("High performance device")
                .seller("Test Seller")
                .location("Warehouse A")
                .price(new BigDecimal("999.99"))
                .stock(50)
                .manufacturingDate(testDate)
                .expiryDate(LocalDate.of(2026, 6, 6))
                .createdAt(testDateTime)
                .updatedAt(testDateTime)
                .build();
    }

    @Test
    void testConvertToDtoWithAllFields() {
        InventoryResponseDTO result = InventoryUtil.convertToDto(inventory);

        assertNotNull(result, "DTO should not be null");
        assertEquals(inventory.getId(), result.getId(), "ID should match");
        assertEquals(inventory.getName(), result.getName(), "Name should match");
        assertEquals(inventory.getCategory(), result.getCategory(), "Category should match");
        assertEquals(inventory.getSubCategory(), result.getSubCategory(), "SubCategory should match");
        assertEquals(inventory.getModel(), result.getModel(), "Model should match");
        assertEquals(inventory.getSpecification(), result.getSpecification(), "Specification should match");
        assertEquals(inventory.getSeller(), result.getSeller(), "Seller should match");
        assertEquals(inventory.getLocation(), result.getLocation(), "Location should match");
        assertEquals(inventory.getPrice(), result.getPrice(), "Price should match");
        assertEquals(inventory.getStock(), result.getStock(), "Stock should match");
        assertEquals(inventory.getManufacturingDate(), result.getManufacturingDate(), "Manufacturing date should match");
        assertEquals(inventory.getExpiryDate(), result.getExpiryDate(), "Expiry date should match");
        assertEquals(inventory.getCreatedAt(), result.getCreatedAt(), "Created at should match");
        assertEquals(inventory.getUpdatedAt(), result.getUpdatedAt(), "Updated at should match");
    }

    @Test
    void testConvertToDtoWithNullOptionalFields() {
        inventory.setCategory(null);
        inventory.setSubCategory(null);
        inventory.setModel(null);
        inventory.setSpecification(null);
        inventory.setSeller(null);
        inventory.setLocation(null);
        inventory.setManufacturingDate(null);
        inventory.setExpiryDate(null);

        InventoryResponseDTO result = InventoryUtil.convertToDto(inventory);

        assertNotNull(result, "DTO should not be null");
        assertEquals(1L, result.getId());
        assertEquals("Test Product", result.getName());
        assertNull(result.getCategory(), "Category should be null");
        assertNull(result.getSubCategory(), "SubCategory should be null");
        assertNull(result.getModel(), "Model should be null");
        assertNull(result.getSpecification(), "Specification should be null");
        assertNull(result.getSeller(), "Seller should be null");
        assertNull(result.getLocation(), "Location should be null");
        assertNull(result.getManufacturingDate(), "Manufacturing date should be null");
        assertNull(result.getExpiryDate(), "Expiry date should be null");
    }

    @Test
    void testConvertToDtoWithMinimumData() {
        Inventory minimalInventory = Inventory.builder()
                .id(2L)
                .name("Minimal Product")
                .price(BigDecimal.ONE)
                .stock(1)
                .createdAt(testDateTime)
                .updatedAt(testDateTime)
                .build();

        InventoryResponseDTO result = InventoryUtil.convertToDto(minimalInventory);

        assertNotNull(result);
        assertEquals(2L, result.getId());
        assertEquals("Minimal Product", result.getName());
        assertEquals(BigDecimal.ONE, result.getPrice());
        assertEquals(1, result.getStock());
        assertNull(result.getCategory());
        assertNull(result.getSubCategory());
        assertNull(result.getManufacturingDate());
        assertNull(result.getExpiryDate());
    }

    @Test
    void testConvertToDtoWithZeroStock() {
        inventory.setStock(0);

        InventoryResponseDTO result = InventoryUtil.convertToDto(inventory);

        assertNotNull(result);
        assertEquals(0, result.getStock(), "Stock quantity should be zero");
    }

    @Test
    void testConvertToDtoWithLargeStockQuantity() {
        inventory.setStock(Integer.MAX_VALUE);

        InventoryResponseDTO result = InventoryUtil.convertToDto(inventory);

        assertNotNull(result);
        assertEquals(Integer.MAX_VALUE, result.getStock(), "Stock should be maximum integer value");
    }

    @Test
    void testConvertToDtoWithHighPrice() {
        inventory.setPrice(new BigDecimal("999999.99"));

        InventoryResponseDTO result = InventoryUtil.convertToDto(inventory);

        assertNotNull(result);
        assertEquals(new BigDecimal("999999.99"), result.getPrice(), "Price should match with decimal precision");
    }

    @Test
    void testConvertToDtoWithZeroPrice() {
        inventory.setPrice(BigDecimal.ZERO);

        InventoryResponseDTO result = InventoryUtil.convertToDto(inventory);

        assertNotNull(result);
        assertEquals(BigDecimal.ZERO, result.getPrice(), "Price should be zero");
    }

    @Test
    void testConvertToDtoWithEmptyStringFields() {
        inventory.setName("");
        inventory.setCategory("");
        inventory.setModel("");
        inventory.setSeller("");

        InventoryResponseDTO result = InventoryUtil.convertToDto(inventory);

        assertNotNull(result);
        assertEquals("", result.getName(), "Name should be empty string");
        assertEquals("", result.getCategory(), "Category should be empty string");
        assertEquals("", result.getModel(), "Model should be empty string");
        assertEquals("", result.getSeller(), "Seller should be empty string");
    }

    @Test
    void testConvertToDtoWithLongSpecification() {
        String longSpecification = "A".repeat(500); // 500 characters
        inventory.setSpecification(longSpecification);

        InventoryResponseDTO result = InventoryUtil.convertToDto(inventory);

        assertNotNull(result);
        assertEquals(longSpecification, result.getSpecification(), "Long specification should be preserved");
        assertEquals(500, result.getSpecification().length());
    }

    @Test
    void testConvertToDtoWithSpecialCharacters() {
        inventory.setName("Product @#$%^&*()");
        inventory.setCategory("Category-123/456");
        inventory.setSeller("Seller & Co.");

        InventoryResponseDTO result = InventoryUtil.convertToDto(inventory);

        assertNotNull(result);
        assertEquals("Product @#$%^&*()", result.getName());
        assertEquals("Category-123/456", result.getCategory());
        assertEquals("Seller & Co.", result.getSeller());
    }

    @Test
    void testConvertToDtoWithUnicodeCharacters() {
        inventory.setName("产品名称");
        inventory.setCategory("電子機器");
        inventory.setSeller("বিক্রেতা");

        InventoryResponseDTO result = InventoryUtil.convertToDto(inventory);

        assertNotNull(result);
        assertEquals("产品名称", result.getName());
        assertEquals("電子機器", result.getCategory());
        assertEquals("বিক্রেতা", result.getSeller());
    }

    @Test
    void testConvertToDtoWithLargeID() {
        inventory.setId(Long.MAX_VALUE);

        InventoryResponseDTO result = InventoryUtil.convertToDto(inventory);

        assertNotNull(result);
        assertEquals(Long.MAX_VALUE, result.getId(), "ID should be maximum long value");
    }

    @Test
    void testConvertToDtoMaintainsDateTimePrecision() {

        LocalDateTime preciseDateTime = LocalDateTime.of(2024, 12, 31, 23, 59, 59, 999999999);
        inventory.setCreatedAt(preciseDateTime);
        inventory.setUpdatedAt(preciseDateTime);
        inventory.setManufacturingDate(LocalDate.of(2024, 12, 31));
        inventory.setExpiryDate(LocalDate.of(2025, 1, 1));

        InventoryResponseDTO result = InventoryUtil.convertToDto(inventory);

        assertNotNull(result);
        assertEquals(preciseDateTime, result.getCreatedAt(), "CreatedAt should maintain precision");
        assertEquals(preciseDateTime, result.getUpdatedAt(), "UpdatedAt should maintain precision");
        assertEquals(LocalDate.of(2024, 12, 31), result.getManufacturingDate());
        assertEquals(LocalDate.of(2025, 1, 1), result.getExpiryDate());
    }

    @Test
    void testConvertToDtoWithNullInventory() {
        assertThrows(NullPointerException.class,
            () -> InventoryUtil.convertToDto(null),
            "Should throw NullPointerException when inventory is null");
    }

    @Test
    void testConvertToDtoIDMapping() {
        for (long testId : new long[]{1L, 100L, 999999L}) {
            inventory.setId(testId);

            InventoryResponseDTO result = InventoryUtil.convertToDto(inventory);

            assertEquals(testId, result.getId(), "ID should be correctly mapped");
        }
    }

    @Test
    void testMultipleConsecutiveConversions() {
        InventoryResponseDTO result1 = InventoryUtil.convertToDto(inventory);
        InventoryResponseDTO result2 = InventoryUtil.convertToDto(inventory);
        InventoryResponseDTO result3 = InventoryUtil.convertToDto(inventory);

        assertNotNull(result1);
        assertNotNull(result2);
        assertNotNull(result3);
        assertEquals(result1.getId(), result2.getId());
        assertEquals(result2.getId(), result3.getId());
        assertEquals(result1.getName(), result2.getName());
        assertEquals(result2.getName(), result3.getName());
    }

    @Test
    void testConvertMultipleDifferentInventories() {
        Inventory inventory1 = Inventory.builder()
                .id(1L)
                .name("Product 1")
                .price(BigDecimal.TEN)
                .stock(10)
                .createdAt(testDateTime)
                .updatedAt(testDateTime)
                .build();

        Inventory inventory2 = Inventory.builder()
                .id(2L)
                .name("Product 2")
                .price(BigDecimal.valueOf(20))
                .stock(20)
                .createdAt(testDateTime)
                .updatedAt(testDateTime)
                .build();

        InventoryResponseDTO dto1 = InventoryUtil.convertToDto(inventory1);
        InventoryResponseDTO dto2 = InventoryUtil.convertToDto(inventory2);

        assertNotNull(dto1);
        assertNotNull(dto2);
        assertNotEquals(dto1.getId(), dto2.getId(), "Different inventories should have different IDs");
        assertNotEquals(dto1.getName(), dto2.getName(), "Different inventories should have different names");
        assertEquals(1L, dto1.getId());
        assertEquals(2L, dto2.getId());
        assertEquals("Product 1", dto1.getName());
        assertEquals("Product 2", dto2.getName());
    }

    @Test
    void testBuildNoRecordsMessageWithCriteria() {
        InventoryFilter filter = new InventoryFilter();
        filter.setName("Test Product");
        filter.setCategory("Electronics");
        filter.setSubCategory("Mobile Devices");
        filter.setSeller("Test Seller");
        filter.setLocation("Warehouse A");
        filter.setMinPrice(new java.math.BigDecimal("100.00"));
        filter.setMaxPrice(new java.math.BigDecimal("200.00"));

        String message = InventoryUtil.buildNoRecordsMessage(filter);

        String expected = "No inventory records found matching filter criteria [name=Test Product, category=Electronics, subCategory=Mobile Devices, seller=Test Seller, location=Warehouse A, minPrice=100.00, maxPrice=200.00]";
        assertEquals(expected, message);
    }

    @Test
    void testBuildNoRecordsMessageWithNoCriteria() {
        InventoryFilter filter = new InventoryFilter();

        String message = InventoryUtil.buildNoRecordsMessage(filter);

        assertEquals("No inventory records found matching filter criteria []", message);
    }

    @Test
    void testBuildNoRecordsMessageWithNullFilter() {
        assertThrows(NullPointerException.class, () -> InventoryUtil.buildNoRecordsMessage(null));
    }
}

