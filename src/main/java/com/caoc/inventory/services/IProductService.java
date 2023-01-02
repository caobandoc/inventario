package com.caoc.inventory.services;

import com.caoc.inventory.model.documents.Producto;
import com.caoc.inventory.model.response.ProductResponseRest;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public interface IProductService {
    public Mono<ResponseEntity<ProductResponseRest>> save(Producto producto, String idCategory);
}
