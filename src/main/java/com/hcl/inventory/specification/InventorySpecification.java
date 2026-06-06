package com.hcl.inventory.specification;

import com.hcl.inventory.entity.Inventory;
import com.hcl.inventory.filter.InventoryFilter;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class InventorySpecification {

    private InventorySpecification() {
    }

    public static Specification<Inventory> build(InventoryFilter filter) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (filter.getId() != null) {
                predicates.add(
                        cb.equal(root.get("id"), filter.getId())
                );
            }

            if (hasText(filter.getName())) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("name")),
                                "%" + filter.getName().toLowerCase() + "%"
                        )
                );
            }

            if (hasText(filter.getCategory())) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("category")),
                                "%" + filter.getCategory().toLowerCase() + "%"
                        )
                );
            }

            if (hasText(filter.getSubCategory())) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("subCategory")),
                                "%" + filter.getSubCategory().toLowerCase() + "%"
                        )
                );
            }

            if (hasText(filter.getModel())) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("model")),
                                "%" + filter.getModel().toLowerCase() + "%"
                        )
                );
            }

            if (hasText(filter.getSpecification())) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("specification")),
                                "%" + filter.getSpecification().toLowerCase() + "%"
                        )
                );
            }

            if (hasText(filter.getSeller())) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("seller")),
                                "%" + filter.getSeller().toLowerCase() + "%"
                        )
                );
            }

            if (hasText(filter.getLocation())) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("location")),
                                "%" + filter.getLocation().toLowerCase() + "%"
                        )
                );
            }

            if (filter.getMinPrice() != null) {
                predicates.add(
                        cb.greaterThanOrEqualTo(
                                root.get("price"),
                                filter.getMinPrice()
                        )
                );
            }

            if (filter.getMaxPrice() != null) {
                predicates.add(
                        cb.lessThanOrEqualTo(
                                root.get("price"),
                                filter.getMaxPrice()
                        )
                );
            }

            if (filter.getMinStock() != null) {
                predicates.add(
                        cb.greaterThanOrEqualTo(
                                root.get("stock"),
                                filter.getMinStock()
                        )
                );
            }

            if (filter.getMaxStock() != null) {
                predicates.add(
                        cb.lessThanOrEqualTo(
                                root.get("stock"),
                                filter.getMaxStock()
                        )
                );
            }

            if (filter.getManufacturingDateFrom() != null) {
                predicates.add(
                        cb.greaterThanOrEqualTo(
                                root.get("manufacturingDate"),
                                filter.getManufacturingDateFrom()
                        )
                );
            }

            if (filter.getManufacturingDateTo() != null) {
                predicates.add(
                        cb.lessThanOrEqualTo(
                                root.get("manufacturingDate"),
                                filter.getManufacturingDateTo()
                        )
                );
            }

            if (filter.getExpiryDateFrom() != null) {
                predicates.add(
                        cb.greaterThanOrEqualTo(
                                root.get("expiryDate"),
                                filter.getExpiryDateFrom()
                        )
                );
            }

            if (filter.getExpiryDateTo() != null) {
                predicates.add(
                        cb.lessThanOrEqualTo(
                                root.get("expiryDate"),
                                filter.getExpiryDateTo()
                        )
                );
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }
}