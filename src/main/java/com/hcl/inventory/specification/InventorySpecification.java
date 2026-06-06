package com.hcl.inventory.specification;

import com.hcl.inventory.entity.Inventory;
import com.hcl.inventory.filter.InventoryFilter;
import jakarta.persistence.criteria.Predicate;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class InventorySpecification {

    public static Specification<Inventory> build(InventoryFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            addIdPredicate(filter, root, cb, predicates);
            addTextPredicates(filter, root, cb, predicates);
            addPriceRangePredicates(filter, root, cb, predicates);
            addStockRangePredicates(filter, root, cb, predicates);
            addManufacturingDatePredicates(filter, root, cb, predicates);
            addExpiryDatePredicates(filter, root, cb, predicates);
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static void addIdPredicate(InventoryFilter filter,
            jakarta.persistence.criteria.Root<Inventory> root,
            jakarta.persistence.criteria.CriteriaBuilder cb,
            List<Predicate> predicates) {
        if (filter.getId() != null) {
            predicates.add(cb.equal(root.get("id"), filter.getId()));
        }
    }

    private static void addTextPredicates(InventoryFilter filter,
            jakarta.persistence.criteria.Root<Inventory> root,
            jakarta.persistence.criteria.CriteriaBuilder cb,
            List<Predicate> predicates) {
        addTextPredicate(filter.getName(), "name", root, cb, predicates);
        addTextPredicate(filter.getCategory(), "category", root, cb, predicates);
        addTextPredicate(filter.getSubCategory(), "subCategory", root, cb, predicates);
        addTextPredicate(filter.getModel(), "model", root, cb, predicates);
        addTextPredicate(filter.getSpecification(), "specification", root, cb, predicates);
        addTextPredicate(filter.getSeller(), "seller", root, cb, predicates);
        addTextPredicate(filter.getLocation(), "location", root, cb, predicates);
    }

    private static void addTextPredicate(String value, String fieldName,
            jakarta.persistence.criteria.Root<Inventory> root,
            jakarta.persistence.criteria.CriteriaBuilder cb,
            List<Predicate> predicates) {
        if (hasText(value)) {
            predicates.add(cb.like(cb.lower(root.get(fieldName)), "%" + value.toLowerCase() + "%"));
        }
    }

    private static void addPriceRangePredicates(InventoryFilter filter,
            jakarta.persistence.criteria.Root<Inventory> root,
            jakarta.persistence.criteria.CriteriaBuilder cb,
            List<Predicate> predicates) {
        if (filter.getMinPrice() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("price"), filter.getMinPrice()));
        }
        if (filter.getMaxPrice() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("price"), filter.getMaxPrice()));
        }
    }

    private static void addStockRangePredicates(InventoryFilter filter,
            jakarta.persistence.criteria.Root<Inventory> root,
            jakarta.persistence.criteria.CriteriaBuilder cb,
            List<Predicate> predicates) {
        if (filter.getMinStock() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("stock"), filter.getMinStock()));
        }
        if (filter.getMaxStock() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("stock"), filter.getMaxStock()));
        }
    }

    private static void addManufacturingDatePredicates(InventoryFilter filter,
            jakarta.persistence.criteria.Root<Inventory> root,
            jakarta.persistence.criteria.CriteriaBuilder cb,
            List<Predicate> predicates) {
        if (filter.getManufacturingDateFrom() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("manufacturingDate"), filter.getManufacturingDateFrom()));
        }
        if (filter.getManufacturingDateTo() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("manufacturingDate"), filter.getManufacturingDateTo()));
        }
    }

    private static void addExpiryDatePredicates(InventoryFilter filter,
            jakarta.persistence.criteria.Root<Inventory> root,
            jakarta.persistence.criteria.CriteriaBuilder cb,
            List<Predicate> predicates) {
        if (filter.getExpiryDateFrom() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("expiryDate"), filter.getExpiryDateFrom()));
        }
        if (filter.getExpiryDateTo() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("expiryDate"), filter.getExpiryDateTo()));
        }
    }

    private static boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }
}