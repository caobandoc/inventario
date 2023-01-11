package com.caoc.inventory.controller;

import com.caoc.inventory.model.documents.Category;
import com.caoc.inventory.model.response.CategoryResponseRest;
import com.caoc.inventory.services.ICategoryService;
import com.caoc.inventory.util.CategoryExcelExporter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.io.IOException;

@CrossOrigin(origins = {"http://localhost:4200"})
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
    @PutMapping("/{id}")
    public Mono<ResponseEntity<CategoryResponseRest>> saveCategory(@RequestBody Category category, @PathVariable String id) {
        return categoryService.update(category, id);
    }
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<CategoryResponseRest>> deleteCategory(@PathVariable String id) {
        return categoryService.delete(id);
    }

    @GetMapping("/export/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=result_categories.xlsx";
        response.setHeader(headerKey, headerValue);

        Mono<ResponseEntity<CategoryResponseRest>> search = categoryService.search();

        CategoryExcelExporter excelExporter = new CategoryExcelExporter(search.block().getBody().getCategoryResponse().getCategoryList());

        excelExporter.export(response);
    }
}
