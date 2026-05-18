package com.example.productjpa.controller;

import com.example.productjpa.entity.Product;
import com.example.productjpa.service.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // =========================
    // HOME REDIRECT
    // =========================
    @GetMapping("/")
    public String homeRedirect() {
        return "redirect:/products";
    }

    // =========================
    // LIST PRODUCTS
    // =========================
    @GetMapping
    public String listProducts(Model model) {

        model.addAttribute("products",
                productService.findAll());

        return "product/list";
    }

    // =========================
    // SEARCH PRODUCTS
    // =========================
    @GetMapping("/search")
    public String searchProducts(
            @RequestParam(value = "keyword", required = false) String keyword,
            Model model) {

        if (keyword == null || keyword.trim().isEmpty()) {

            model.addAttribute("products",
                    productService.findAll());

        } else {

            model.addAttribute("products",
                    productService.searchByName(keyword.trim()));
        }

        model.addAttribute("keyword", keyword);

        return "product/list";
    }

    // =========================
    // SHOW CREATE FORM
    // =========================
    @GetMapping("/new")
    public String showCreateForm(Model model) {

        model.addAttribute("product", new Product());
        model.addAttribute("pageTitle", "Add Product");

        return "product/form";
    }

    // =========================
    // SAVE PRODUCT
    // =========================
    @PostMapping("/save")
    public String saveProduct(
            @ModelAttribute Product product,
            @RequestParam("imageFile") MultipartFile imageFile,
            RedirectAttributes redirectAttributes) {

        productService.addProduct(product, imageFile);

        redirectAttributes.addFlashAttribute(
                "success",
                "Product added successfully!"
        );

        return "redirect:/products";
    }

    // =========================
    // VIEW PRODUCT
    // =========================
    @GetMapping("/view/{id}")
    public String viewProduct(
            @PathVariable Long id,
            Model model,
            RedirectAttributes redirectAttributes) {

        Product product = productService.getById(id);

        if (product == null) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    "Product not found!"
            );

            return "redirect:/products";
        }

        model.addAttribute("product", product);

        return "product/view";
    }

    // =========================
    // EDIT PRODUCT
    // =========================
    @GetMapping("/edit/{id}")
    public String editProduct(
            @PathVariable Long id,
            Model model,
            RedirectAttributes redirectAttributes) {

        Product product = productService.getById(id);

        if (product == null) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    "Product not found!"
            );

            return "redirect:/products";
        }

        model.addAttribute("product", product);
        model.addAttribute("pageTitle", "Edit Product");

        return "product/form";
    }

    // =========================
    // UPDATE PRODUCT
    // =========================
    @PostMapping("/update/{id}")
    public String updateProduct(
            @PathVariable Long id,
            @ModelAttribute Product product,
            @RequestParam("imageFile") MultipartFile imageFile,
            RedirectAttributes redirectAttributes) {

        product.setId(id);
        productService.updateProduct(product, imageFile);

        redirectAttributes.addFlashAttribute(
                "success",
                "Product updated successfully!"
        );

        return "redirect:/products";
    }

    // =========================
    // DELETE PRODUCT
    // =========================
    @GetMapping("/delete/{id}")
    public String deleteProduct(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        productService.deleteById(id);

        redirectAttributes.addFlashAttribute(
                "success",
                "Product deleted successfully!"
        );

        return "redirect:/products";
    }
}