package com.example.productjpa.service;

import com.example.productjpa.entity.Product;
import com.example.productjpa.repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // Upload Folder
    private static final String UPLOAD_DIR =
            System.getProperty("user.dir") + "/uploads/";

    // =========================
    // LIST ALL PRODUCTS
    // =========================
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    // =========================
    // GET PRODUCT BY ID
    // =========================
    public Product getById(Long id) {

        return productRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Product not found"));
    }

    // =========================
    // SEARCH PRODUCT BY NAME
    // =========================
    public List<Product> searchByName(String keyword) {

        return productRepository
                .findByNameContainingIgnoreCase(keyword);
    }

    // =========================
    // ADD PRODUCT
    // =========================
    public void addProduct(Product product,
                           MultipartFile imageFile) {

        try {

            // Upload Image
            if (imageFile != null &&
                    !imageFile.isEmpty()) {

                String fileName =
                        uploadImage(imageFile);

                product.setImagename(fileName);
            }

            productRepository.save(product);

        } catch (Exception e) {

            throw new RuntimeException(
                    "Error saving product : "
                            + e.getMessage());

        }
    }

    // =========================
    // UPDATE PRODUCT
    // =========================
    public void updateProduct(Product product,
                              MultipartFile imageFile) {

        try {

            Product oldProduct =
                    getById(product.getId());

            // New Image Upload
            if (imageFile != null &&
                    !imageFile.isEmpty()) {

                // Delete old image
                deleteImage(oldProduct.getImagename());

                // Upload new image
                String fileName =
                        uploadImage(imageFile);

                product.setImagename(fileName);

            } else {

                // Keep old image
                product.setImagename(
                        oldProduct.getImagename());
            }

            productRepository.save(product);

        } catch (Exception e) {

            throw new RuntimeException(
                    "Error updating product : "
                            + e.getMessage());

        }
    }

    // =========================
    // DELETE PRODUCT
    // =========================
    public void deleteById(Long id) {

        Product product = getById(id);

        // Delete image
        deleteImage(product.getImagename());

        // Delete database record
        productRepository.deleteById(id);
    }

    // =========================
    // IMAGE UPLOAD METHOD
    // =========================
    private String uploadImage(
            MultipartFile imageFile)
            throws IOException {

        // Create Upload Folder
        File dir = new File(UPLOAD_DIR);

        if (!dir.exists()) {
            dir.mkdirs();
        }

        // Unique File Name
        String fileName =
                UUID.randomUUID()
                        + "_"
                        + imageFile.getOriginalFilename();

        // Save File
        Path path =
                Paths.get(UPLOAD_DIR + fileName);

        Files.write(path, imageFile.getBytes());

        return fileName;
    }

    // =========================
    // DELETE IMAGE METHOD
    // =========================
    private void deleteImage(String imageName) {

        try {

            if (imageName != null &&
                    !imageName.isEmpty()) {

                Path path =
                        Paths.get(UPLOAD_DIR + imageName);

                Files.deleteIfExists(path);
            }

        } catch (IOException e) {

            System.out.println(
                    "Image delete failed : "
                            + e.getMessage());
        }
    }

}