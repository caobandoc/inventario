package com.caoc.inventory.services;

import com.caoc.inventory.model.documents.Category;
import com.caoc.inventory.model.response.CategoryResponseRest;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public interface ICategoryService {

    Mono<ResponseEntity<CategoryResponseRest>> search();
    Mono<ResponseEntity<CategoryResponseRest>> searchById(String id);
    Mono<ResponseEntity<CategoryResponseRest>> save(Category category);
    Mono<ResponseEntity<CategoryResponseRest>> update(Category category, String id);
    Mono<ResponseEntity<CategoryResponseRest>> delete(String id);

}
