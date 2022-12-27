package com.caoc.inventory.controller;

import com.caoc.inventory.model.response.CategoryResponseRest;
import com.caoc.inventory.services.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryRestController {
    @Autowired
    private ICategoryService categoryService;
    @GetMapping
    public Mono<ResponseEntity<CategoryResponseRest>> searchCayegories() {
        return categoryService.search();
    }
}
