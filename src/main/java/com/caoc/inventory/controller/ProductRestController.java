package com.caoc.inventory.controller;

import com.caoc.inventory.model.documents.Producto;
import com.caoc.inventory.model.response.ProductResponseRest;
import com.caoc.inventory.services.IProductService;
import com.caoc.inventory.util.Util;
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

    @PostMapping
    public Mono<ResponseEntity<ProductResponseRest>> save(
            @RequestParam MultipartFile picture,
            @RequestParam String name,
            @RequestParam double price,
            @RequestParam int account,
            @RequestParam String categoryId) {
        //crear un producto
        return Mono.just(new Producto())
                .flatMap(product -> {
                    try {
                        product.setName(name);
                        product.setPrice(price);
                        product.setAccount(account);
                        product.setImage(Util.compressZLib(picture.getBytes()));
                        return productService.save(product, categoryId);
                    }catch (IOException e){
                        throw new RuntimeException(e);
                    }
                });

    }
}
