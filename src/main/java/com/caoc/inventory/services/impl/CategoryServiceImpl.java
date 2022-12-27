package com.caoc.inventory.services.impl;

import com.caoc.inventory.model.documents.Category;
import com.caoc.inventory.model.dao.ICategoryDao;
import com.caoc.inventory.model.response.CategoryResponseRest;
import com.caoc.inventory.services.ICategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class CategoryServiceImpl implements ICategoryService {
    @Autowired
    private ICategoryDao categoryDao;
    @Override
    @Transactional(readOnly = true)
    public Mono<ResponseEntity<CategoryResponseRest>> search() {
        return categoryDao.findAll()
                .collectList()
                .flatMap(categoryList -> {
                    CategoryResponseRest response = new CategoryResponseRest();

                    response.getCategoryResponse().setCategoryList(categoryList);
                    response.setMetadata("success", "00", "Respuesta exitosa");

                    return Mono.just(ResponseEntity.ok(response));
                })
                .onErrorResume(error -> {
                    CategoryResponseRest response = new CategoryResponseRest();
                    response.setMetadata("error", "-1", "Error al obtener las categorias");
                    log.error(error.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response));
                });
    }
}
