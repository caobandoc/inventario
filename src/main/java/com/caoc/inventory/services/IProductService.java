package com.caoc.inventory.services;

import com.caoc.inventory.model.documents.Producto;
import com.caoc.inventory.model.response.ProductResponseRest;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public interface IProductService {
    Mono<ResponseEntity<ProductResponseRest>> search();
    Mono<ResponseEntity<ProductResponseRest>> save(Producto producto, String idCategory);
    Mono<ResponseEntity<ProductResponseRest>> update(Producto producto, String idProduct, String idCategory);
    Mono<ResponseEntity<ProductResponseRest>> searchById(String id);
    Mono<ResponseEntity<ProductResponseRest>> searchByName(String name);
    Mono<ResponseEntity<ProductResponseRest>> deleteById(String id);
}
