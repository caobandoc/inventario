package com.caoc.inventory.services;

import com.caoc.inventory.model.response.CategoryResponseRest;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public interface ICategoryService {

    public Mono<ResponseEntity<CategoryResponseRest>> search();

}
