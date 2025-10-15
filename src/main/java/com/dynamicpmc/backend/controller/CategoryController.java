package com.dynamicpmc.backend.controller;

import com.dynamicpmc.backend.entity.Category;
import com.dynamicpmc.backend.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "${app.cors.allowed-origins}")
//@CrossOrigin
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        try {
            return ResponseEntity.ok(categoryService.getAllCategories());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        try {
            return categoryService.getCategoryById(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody Category category) {
        try {
            if (category.getName() == null || category.getName().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Category name is required");
            }
            return ResponseEntity.ok(categoryService.saveCategory(category));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error creating category: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Long id, @RequestBody Category category) {
        try {
            return categoryService.getCategoryById(id)
                    .map(existingCategory -> {
                        if (category.getName() != null) {
                            existingCategory.setName(category.getName());
                        }
                        if (category.getDescription() != null) {
                            existingCategory.setDescription(category.getDescription());
                        }
                        return ResponseEntity.ok(categoryService.saveCategory(existingCategory));
                    })
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error updating category");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        try {
            if (categoryService.getCategoryById(id).isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            categoryService.deleteCategory(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}