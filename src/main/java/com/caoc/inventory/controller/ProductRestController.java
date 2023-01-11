package com.caoc.inventory.controller;

import com.caoc.inventory.model.documents.Producto;
import com.caoc.inventory.model.response.ProductResponseRest;
import com.caoc.inventory.services.IProductService;
import com.caoc.inventory.util.ProductExcelExporter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/api/v1/products")
public class ProductRestController {
    @Autowired
    private IProductService productService;

    @GetMapping
    public Mono<ResponseEntity<ProductResponseRest>> search() {
        return productService.search();
    }

    @PostMapping
    public Mono<ResponseEntity<ProductResponseRest>> save(
            @RequestParam MultipartFile picture,
            @RequestParam String name,
            @RequestParam double price,
            @RequestParam int account,
            @RequestParam String categoryId) {
        return Mono.just(new Producto())
                .flatMap(product -> {
                    try {
                        product.setName(name);
                        product.setPrice(price);
                        product.setAccount(account);
                        product.setPicture(picture.getBytes());
                        return productService.save(product, categoryId);
                    }catch (IOException e){
                        throw new RuntimeException(e);
                    }
                });
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<ProductResponseRest>> update(
            @PathVariable("id") String productId,
            @RequestParam MultipartFile picture,
            @RequestParam String name,
            @RequestParam double price,
            @RequestParam int account,
            @RequestParam String categoryId) {
        return Mono.just(new Producto())
                .flatMap(product -> {
                    try {
                        product.setName(name);
                        product.setPrice(price);
                        product.setAccount(account);
                        product.setPicture(picture.getBytes());
                        return productService.update(product, productId, categoryId);
                    }catch (IOException e){
                        throw new RuntimeException(e);
                    }
                });
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<ProductResponseRest>> searchById(@PathVariable String id){
        return productService.searchById(id);
    }

    @GetMapping("/filter/{name}")
    public Mono<ResponseEntity<ProductResponseRest>> searchByName(@PathVariable String name){
        return productService.searchByName(name);
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<ProductResponseRest>> deleteById(@PathVariable String id){
        return productService.deleteById(id);
    }

    @GetMapping("/export/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=result_categories.xlsx";
        response.setHeader(headerKey, headerValue);

        Mono<ResponseEntity<ProductResponseRest>> search = productService.search();

        ProductExcelExporter excelExporter = new ProductExcelExporter(search.block().getBody().getProductResponse().getProducts());

        excelExporter.export(response);
    }
}
