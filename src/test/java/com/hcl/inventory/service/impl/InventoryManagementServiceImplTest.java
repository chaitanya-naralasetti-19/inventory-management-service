package com.hcl.inventory.service.impl;

import com.hcl.inventory.dto.InventoryResponseDTO;
import com.hcl.inventory.entity.Inventory;
import com.hcl.inventory.filter.InventoryFilter;
import com.hcl.inventory.repository.InventoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryManagementServiceImplTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @InjectMocks
    private InventoryManagementServiceImpl inventoryManagementService;

    private Inventory testInventory;
    private InventoryFilter testFilter;
    private Pageable testPageable;
    private LocalDateTime testDateTime;

    @BeforeEach
    void setUp() {
        LocalDate testDate = LocalDate.of(2024, 6, 6);
        testDateTime = LocalDateTime.of(2024, 6, 6, 10, 30, 0);
        testPageable = PageRequest.of(0, 10, Sort.by("id").ascending());

        testInventory = Inventory.builder()
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

        testFilter = new InventoryFilter();
    }

    @Test
    void testSearchInventoryWithEmptyFilter() {
        List<Inventory> inventoryList = List.of(testInventory);
        Page<Inventory> inventoryPage = new PageImpl<>(inventoryList, testPageable, 1);

        when(inventoryRepository.findAll(any(Specification.class), eq(testPageable)))
                .thenReturn(inventoryPage);
        
        Page<InventoryResponseDTO> result = inventoryManagementService.searchInventory(testFilter, testPageable);
        
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        assertEquals(testInventory.getId(), result.getContent().get(0).getId());
        assertEquals(testInventory.getName(), result.getContent().get(0).getName());
        verify(inventoryRepository, times(1)).findAll(any(Specification.class), eq(testPageable));
    }

    @Test
    void testSearchInventoryReturnsEmptyPage() {
        Page<Inventory> emptyPage = new PageImpl<>(new ArrayList<>(), testPageable, 0);

        when(inventoryRepository.findAll(any(Specification.class), eq(testPageable)))
                .thenReturn(emptyPage);
        
        Page<InventoryResponseDTO> result = inventoryManagementService.searchInventory(testFilter, testPageable);
        
        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        assertTrue(result.getContent().isEmpty());
        verify(inventoryRepository, times(1)).findAll(any(Specification.class), eq(testPageable));
    }

    @Test
    void testSearchInventoryByName() {
        testFilter.setName("Test Product");
        List<Inventory> inventoryList = List.of(testInventory);
        Page<Inventory> inventoryPage = new PageImpl<>(inventoryList, testPageable, 1);

        when(inventoryRepository.findAll(any(Specification.class), eq(testPageable)))
                .thenReturn(inventoryPage);
        
        Page<InventoryResponseDTO> result = inventoryManagementService.searchInventory(testFilter, testPageable);
        
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Test Product", result.getContent().get(0).getName());
        verify(inventoryRepository, times(1)).findAll(any(Specification.class), eq(testPageable));
    }

    @Test
    void testSearchInventoryByCategory() {
        testFilter.setCategory("Electronics");
        List<Inventory> inventoryList = List.of(testInventory);
        Page<Inventory> inventoryPage = new PageImpl<>(inventoryList, testPageable, 1);

        when(inventoryRepository.findAll(any(Specification.class), eq(testPageable)))
                .thenReturn(inventoryPage);
        
        Page<InventoryResponseDTO> result = inventoryManagementService.searchInventory(testFilter, testPageable);
        
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Electronics", result.getContent().get(0).getCategory());
        verify(inventoryRepository, times(1)).findAll(any(Specification.class), eq(testPageable));
    }

    @Test
    void testSearchInventoryByPriceRange() {
        testFilter.setMinPrice(new BigDecimal("500.00"));
        testFilter.setMaxPrice(new BigDecimal("1500.00"));
        List<Inventory> inventoryList = List.of(testInventory);
        Page<Inventory> inventoryPage = new PageImpl<>(inventoryList, testPageable, 1);

        when(inventoryRepository.findAll(any(Specification.class), eq(testPageable)))
                .thenReturn(inventoryPage);
        
        Page<InventoryResponseDTO> result = inventoryManagementService.searchInventory(testFilter, testPageable);
        
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertTrue(result.getContent().get(0).getPrice().compareTo(new BigDecimal("500.00")) >= 0);
        assertTrue(result.getContent().get(0).getPrice().compareTo(new BigDecimal("1500.00")) <= 0);
        verify(inventoryRepository, times(1)).findAll(any(Specification.class), eq(testPageable));
    }

    @Test
    void testSearchInventoryByStockRange() {
        testFilter.setMinStock(10);
        testFilter.setMaxStock(100);
        List<Inventory> inventoryList = List.of(testInventory);
        Page<Inventory> inventoryPage = new PageImpl<>(inventoryList, testPageable, 1);

        when(inventoryRepository.findAll(any(Specification.class), eq(testPageable)))
                .thenReturn(inventoryPage);
        
        Page<InventoryResponseDTO> result = inventoryManagementService.searchInventory(testFilter, testPageable);
        
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertTrue(result.getContent().get(0).getStock() >= 10);
        assertTrue(result.getContent().get(0).getStock() <= 100);
        verify(inventoryRepository, times(1)).findAll(any(Specification.class), eq(testPageable));
    }

    @Test
    void testSearchInventoryWithMultipleFilters() {
        testFilter.setName("Test Product");
        testFilter.setCategory("Electronics");
        testFilter.setMinPrice(new BigDecimal("500.00"));
        testFilter.setMaxPrice(new BigDecimal("1500.00"));
        testFilter.setMinStock(10);
        testFilter.setMaxStock(100);

        List<Inventory> inventoryList = List.of(testInventory);
        Page<Inventory> inventoryPage = new PageImpl<>(inventoryList, testPageable, 1);

        when(inventoryRepository.findAll(any(Specification.class), eq(testPageable)))
                .thenReturn(inventoryPage);
        
        Page<InventoryResponseDTO> result = inventoryManagementService.searchInventory(testFilter, testPageable);
        
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        InventoryResponseDTO dto = result.getContent().get(0);
        assertEquals("Test Product", dto.getName());
        assertEquals("Electronics", dto.getCategory());
        assertTrue(dto.getPrice().compareTo(new BigDecimal("500.00")) >= 0);
        assertTrue(dto.getStock() >= 10);
        verify(inventoryRepository, times(1)).findAll(any(Specification.class), eq(testPageable));
    }

    @Test
    void testSearchInventoryWithMultiplePages() {
        
        List<Inventory> page1Inventory = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            Inventory inv = Inventory.builder()
                    .id((long) i)
                    .name("Product " + i)
                    .price(BigDecimal.valueOf(i * 100))
                    .stock(i * 10)
                    .createdAt(testDateTime)
                    .updatedAt(testDateTime)
                    .build();
            page1Inventory.add(inv);
        }

        Pageable page1Pageable = PageRequest.of(0, 10);
        Page<Inventory> page1Result = new PageImpl<>(page1Inventory, page1Pageable, 25);

        when(inventoryRepository.findAll(any(Specification.class), eq(page1Pageable)))
                .thenReturn(page1Result);
        
        Page<InventoryResponseDTO> result = inventoryManagementService.searchInventory(testFilter, page1Pageable);
        
        assertNotNull(result);
        assertEquals(25, result.getTotalElements());
        assertEquals(10, result.getContent().size());
        assertEquals(3, result.getTotalPages());
        assertEquals(0, result.getNumber());
        assertTrue(result.isFirst());
        assertFalse(result.isLast());
        verify(inventoryRepository, times(1)).findAll(any(Specification.class), eq(page1Pageable));
    }

    @Test
    void testSearchInventoryWithSorting() {
        Sort sort = Sort.by("price").descending();
        Pageable sortedPageable = PageRequest.of(0, 10, sort);

        List<Inventory> inventoryList = List.of(testInventory);
        Page<Inventory> inventoryPage = new PageImpl<>(inventoryList, sortedPageable, 1);

        when(inventoryRepository.findAll(any(Specification.class), eq(sortedPageable)))
                .thenReturn(inventoryPage);
        
        Page<InventoryResponseDTO> result = inventoryManagementService.searchInventory(testFilter, sortedPageable);
        
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(inventoryRepository, times(1)).findAll(any(Specification.class), eq(sortedPageable));
    }

    @Test
    void testSearchInventoryConversionToDtoList() {
        Inventory inv1 = createInventory(1L, "Product 1", "Category 1");
        Inventory inv2 = createInventory(2L, "Product 2", "Category 2");
        Inventory inv3 = createInventory(3L, "Product 3", "Category 3");

        List<Inventory> inventoryList = List.of(inv1, inv2, inv3);
        Page<Inventory> inventoryPage = new PageImpl<>(inventoryList, testPageable, 3);

        when(inventoryRepository.findAll(any(Specification.class), eq(testPageable)))
                .thenReturn(inventoryPage);
        
        Page<InventoryResponseDTO> result = inventoryManagementService.searchInventory(testFilter, testPageable);
        
        assertNotNull(result);
        assertEquals(3, result.getTotalElements());
        assertEquals(3, result.getContent().size());

        for (int i = 0; i < 3; i++) {
            InventoryResponseDTO dto = result.getContent().get(i);
            assertNotNull(dto);
            assertEquals(i + 1L, dto.getId());
            assertEquals("Product " + (i + 1), dto.getName());
            assertEquals("Category " + (i + 1), dto.getCategory());
        }

        verify(inventoryRepository, times(1)).findAll(any(Specification.class), eq(testPageable));
    }

    @Test
    void testSearchInventoryById() {
        
        testFilter.setId(1L);
        List<Inventory> inventoryList = List.of(testInventory);
        Page<Inventory> inventoryPage = new PageImpl<>(inventoryList, testPageable, 1);

        when(inventoryRepository.findAll(any(Specification.class), eq(testPageable)))
                .thenReturn(inventoryPage);

        
        Page<InventoryResponseDTO> result = inventoryManagementService.searchInventory(testFilter, testPageable);

        
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1L, result.getContent().get(0).getId());
        verify(inventoryRepository, times(1)).findAll(any(Specification.class), eq(testPageable));
    }

    @Test
    void testSearchInventoryBySeller() {
        
        testFilter.setSeller("Test Seller");
        List<Inventory> inventoryList = List.of(testInventory);
        Page<Inventory> inventoryPage = new PageImpl<>(inventoryList, testPageable, 1);

        when(inventoryRepository.findAll(any(Specification.class), eq(testPageable)))
                .thenReturn(inventoryPage);

        
        Page<InventoryResponseDTO> result = inventoryManagementService.searchInventory(testFilter, testPageable);

        
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Test Seller", result.getContent().get(0).getSeller());
        verify(inventoryRepository, times(1)).findAll(any(Specification.class), eq(testPageable));
    }

    @Test
    void testSearchInventoryByLocation() {
        
        testFilter.setLocation("Warehouse A");
        List<Inventory> inventoryList = List.of(testInventory);
        Page<Inventory> inventoryPage = new PageImpl<>(inventoryList, testPageable, 1);

        when(inventoryRepository.findAll(any(Specification.class), eq(testPageable)))
                .thenReturn(inventoryPage);

        
        Page<InventoryResponseDTO> result = inventoryManagementService.searchInventory(testFilter, testPageable);

        
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Warehouse A", result.getContent().get(0).getLocation());
        verify(inventoryRepository, times(1)).findAll(any(Specification.class), eq(testPageable));
    }

    @Test
    void testSearchInventoryByManufacturingDateRange() {
        
        testFilter.setManufacturingDateFrom(LocalDate.of(2024, 1, 1));
        testFilter.setManufacturingDateTo(LocalDate.of(2024, 12, 31));

        List<Inventory> inventoryList = List.of(testInventory);
        Page<Inventory> inventoryPage = new PageImpl<>(inventoryList, testPageable, 1);

        when(inventoryRepository.findAll(any(Specification.class), eq(testPageable)))
                .thenReturn(inventoryPage);

        
        Page<InventoryResponseDTO> result = inventoryManagementService.searchInventory(testFilter, testPageable);

        
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(inventoryRepository, times(1)).findAll(any(Specification.class), eq(testPageable));
    }

    @Test
    void testSearchInventoryByExpiryDateRange() {
        
        testFilter.setExpiryDateFrom(LocalDate.of(2025, 1, 1));
        testFilter.setExpiryDateTo(LocalDate.of(2027, 12, 31));

        List<Inventory> inventoryList = List.of(testInventory);
        Page<Inventory> inventoryPage = new PageImpl<>(inventoryList, testPageable, 1);

        when(inventoryRepository.findAll(any(Specification.class), eq(testPageable)))
                .thenReturn(inventoryPage);

        
        Page<InventoryResponseDTO> result = inventoryManagementService.searchInventory(testFilter, testPageable);

        
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(inventoryRepository, times(1)).findAll(any(Specification.class), eq(testPageable));
    }

    @Test
    void testSearchInventoryWithNullFilterFields() {
        
        testFilter.setName(null);
        testFilter.setCategory(null);
        testFilter.setMinPrice(null);
        testFilter.setMaxPrice(null);

        List<Inventory> inventoryList = List.of(testInventory);
        Page<Inventory> inventoryPage = new PageImpl<>(inventoryList, testPageable, 1);

        when(inventoryRepository.findAll(any(Specification.class), eq(testPageable)))
                .thenReturn(inventoryPage);

        assertDoesNotThrow(() -> inventoryManagementService.searchInventory(testFilter, testPageable));

        Page<InventoryResponseDTO> result = inventoryManagementService.searchInventory(testFilter, testPageable);
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(inventoryRepository, times(2)).findAll(any(Specification.class), eq(testPageable));
    }

    @Test
    void testSearchInventoryFirstPage() {
        
        Pageable firstPage = PageRequest.of(0, 10);
        List<Inventory> inventoryList = List.of(testInventory);
        Page<Inventory> inventoryPage = new PageImpl<>(inventoryList, firstPage, 1);

        when(inventoryRepository.findAll(any(Specification.class), eq(firstPage)))
                .thenReturn(inventoryPage);

        
        Page<InventoryResponseDTO> result = inventoryManagementService.searchInventory(testFilter, firstPage);

        
        assertNotNull(result);
        assertTrue(result.isFirst());
        assertEquals(0, result.getNumber());
        verify(inventoryRepository, times(1)).findAll(any(Specification.class), eq(firstPage));
    }

    @Test
    void testSearchInventoryLastPage() {
        
        Pageable lastPage = PageRequest.of(2, 10);
        List<Inventory> inventoryList = List.of(testInventory);
        Page<Inventory> inventoryPage = new PageImpl<>(inventoryList, lastPage, 25);

        when(inventoryRepository.findAll(any(Specification.class), eq(lastPage)))
                .thenReturn(inventoryPage);

        
        Page<InventoryResponseDTO> result = inventoryManagementService.searchInventory(testFilter, lastPage);

        
        assertNotNull(result);
        assertTrue(result.isLast());
        assertEquals(2, result.getNumber());
        verify(inventoryRepository, times(1)).findAll(any(Specification.class), eq(lastPage));
    }

    @Test
    void testSearchInventoryBySubCategory() {
        
        testFilter.setSubCategory("Mobile Devices");
        List<Inventory> inventoryList = List.of(testInventory);
        Page<Inventory> inventoryPage = new PageImpl<>(inventoryList, testPageable, 1);

        when(inventoryRepository.findAll(any(Specification.class), eq(testPageable)))
                .thenReturn(inventoryPage);

        
        Page<InventoryResponseDTO> result = inventoryManagementService.searchInventory(testFilter, testPageable);

        
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Mobile Devices", result.getContent().get(0).getSubCategory());
        verify(inventoryRepository, times(1)).findAll(any(Specification.class), eq(testPageable));
    }

    @Test
    void testSearchInventoryByModel() {
        
        testFilter.setModel("Model X");
        List<Inventory> inventoryList = List.of(testInventory);
        Page<Inventory> inventoryPage = new PageImpl<>(inventoryList, testPageable, 1);

        when(inventoryRepository.findAll(any(Specification.class), eq(testPageable)))
                .thenReturn(inventoryPage);

        
        Page<InventoryResponseDTO> result = inventoryManagementService.searchInventory(testFilter, testPageable);

        
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Model X", result.getContent().get(0).getModel());
        verify(inventoryRepository, times(1)).findAll(any(Specification.class), eq(testPageable));
    }

    @Test
    void testSearchInventoryBySpecification() {
        
        testFilter.setSpecification("High performance");
        List<Inventory> inventoryList = List.of(testInventory);
        Page<Inventory> inventoryPage = new PageImpl<>(inventoryList, testPageable, 1);

        when(inventoryRepository.findAll(any(Specification.class), eq(testPageable)))
                .thenReturn(inventoryPage);

        
        Page<InventoryResponseDTO> result = inventoryManagementService.searchInventory(testFilter, testPageable);

        
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("High performance device", result.getContent().get(0).getSpecification());
        verify(inventoryRepository, times(1)).findAll(any(Specification.class), eq(testPageable));
    }

    @Test
    void testSpecificationBuilderIsCalledWithCorrectFilter() {
        
        testFilter.setName("Test");
        List<Inventory> inventoryList = new ArrayList<>();
        Page<Inventory> inventoryPage = new PageImpl<>(inventoryList, testPageable, 0);

        when(inventoryRepository.findAll(any(Specification.class), eq(testPageable)))
                .thenReturn(inventoryPage);

        
        inventoryManagementService.searchInventory(testFilter, testPageable);

        
        verify(inventoryRepository, times(1)).findAll(any(Specification.class), eq(testPageable));
    }

    @Test
    void testPageSizeIsRespected() {
        
        Pageable smallPage = PageRequest.of(0, 5);
        List<Inventory> inventoryList = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            inventoryList.add(createInventory((long) i, "Product " + i, "Category " + i));
        }
        Page<Inventory> inventoryPage = new PageImpl<>(inventoryList, smallPage, 15);

        when(inventoryRepository.findAll(any(Specification.class), eq(smallPage)))
                .thenReturn(inventoryPage);

        
        Page<InventoryResponseDTO> result = inventoryManagementService.searchInventory(testFilter, smallPage);

        
        assertNotNull(result);
        assertEquals(5, result.getContent().size());
        assertEquals(15, result.getTotalElements());
        assertEquals(3, result.getTotalPages());
        verify(inventoryRepository, times(1)).findAll(any(Specification.class), eq(smallPage));
    }

    // Helper method to create test inventory
    private Inventory createInventory(Long id, String name, String category) {
        return Inventory.builder()
                .id(id)
                .name(name)
                .category(category)
                .price(BigDecimal.valueOf(100))
                .stock(10)
                .createdAt(testDateTime)
                .updatedAt(testDateTime)
                .build();
    }
}

