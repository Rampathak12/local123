package com.example.productjpa.repository;

import com.example.productjpa.entity.Product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository
        extends JpaRepository<Product, Long> {

    // =========================
    // SEARCH BY NAME
    // =========================
    List<Product>
    findByNameContainingIgnoreCase(String keyword);

    // =========================
    // FIND EXACT NAME
    // =========================
    Product findByName(String name);

    // =========================
    // SEARCH IN DETAILS
    // =========================
    List<Product>
    findByDetailsContainingIgnoreCase(String keyword);

    // =========================
    // PRICE FILTER
    // =========================
    List<Product>
    findByPriceBetween(double min,
                       double max);

    // =========================
    // PRODUCTS IN STOCK
    // =========================
    List<Product>
    findByStockGreaterThan(int stock);

    // =========================
    // SORT BY PRICE ASC
    // =========================
    List<Product>
    findAllByOrderByPriceAsc();

    // =========================
    // SORT BY PRICE DESC
    // =========================
    List<Product>
    findAllByOrderByPriceDesc();

}