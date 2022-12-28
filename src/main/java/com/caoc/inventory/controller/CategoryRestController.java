package com.caoc.inventory.controller;

import com.caoc.inventory.model.documents.Category;
import com.caoc.inventory.model.response.CategoryResponseRest;
import com.caoc.inventory.services.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryRestController {
    @Autowired
    private ICategoryService categoryService;
    @GetMapping
    public Mono<ResponseEntity<CategoryResponseRest>> searchCategories() {
        return categoryService.search();
    }
    @GetMapping("/{id}")
    public Mono<ResponseEntity<CategoryResponseRest>> searchCategoriesById(@PathVariable String id) {
        return categoryService.searchById(id);
    }

    @PostMapping
    public Mono<ResponseEntity<CategoryResponseRest>> saveCategory(@RequestBody Category category) {
        return categoryService.save(category);
    }
}
