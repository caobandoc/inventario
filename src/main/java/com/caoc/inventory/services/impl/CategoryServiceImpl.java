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

import java.util.Arrays;
import java.util.List;

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
                    CategoryResponseRest response = responseSuccess(categoryList, "Respuesta exitosa");
                    return Mono.just(ResponseEntity.ok(response));
                })

                .onErrorResume(error -> {
                    CategoryResponseRest response = responseError("Error al obtener las categorias");
                    log.error(error.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response));
                });
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<ResponseEntity<CategoryResponseRest>> searchById(String id) {
        return categoryDao.findById(id)
                .flatMap(category -> {
                    CategoryResponseRest response = responseSuccess(Arrays.asList(category), "Categoria encontrada");
                    return Mono.just(ResponseEntity.ok(response));
                })

                .switchIfEmpty(
                        Mono.just(new ResponseEntity<>(responseError("Categoria no encontrada"), HttpStatus.NOT_FOUND))
                )

                .onErrorResume(error -> {
                    CategoryResponseRest response = responseError("Error al obtener la categoria");
                    log.error(error.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response));
                });
    }

    @Override
    @Transactional
    public Mono<ResponseEntity<CategoryResponseRest>> save(Category category) {
        return categoryDao.save(category)
                .flatMap(categorySaved -> {
                    CategoryResponseRest response = responseSuccess(Arrays.asList(categorySaved), "Categoria guardada");
                    return Mono.just(ResponseEntity.ok(response));
                })

                .onErrorResume(error -> {
                    CategoryResponseRest response = responseError("Error al guardar la categoria");
                    log.error(error.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response));
                });
    }

    @Override
    @Transactional
    public Mono<ResponseEntity<CategoryResponseRest>> update(Category category, String id) {
        return categoryDao.findById(id)
                .flatMap(categoryFound -> {
                    categoryFound.setName(category.getName());
                    categoryFound.setDescription(category.getDescription());
                    return categoryDao.save(categoryFound);
                })

                .flatMap(categoryUpdated -> {
                    CategoryResponseRest response = responseSuccess(Arrays.asList(categoryUpdated), "Categoria actualizada");
                    return Mono.just(ResponseEntity.ok(response));
                })

                .switchIfEmpty(
                        Mono.just(new ResponseEntity<>(responseError("Categoria no encontrada"), HttpStatus.NOT_FOUND))
                )

                .onErrorResume(error -> {
                    CategoryResponseRest response = responseError("Error al actualizar la categoria");
                    log.error(error.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response));
                });
    }

    @Override
    @Transactional
    public Mono<ResponseEntity<CategoryResponseRest>> delete(String id) {
        return categoryDao.findById(id)
                .flatMap(categoryFound -> categoryDao.delete(categoryFound)
                        .then(Mono.just(new ResponseEntity<>(responseSuccess(Arrays.asList(categoryFound), "Categoria eliminada"), HttpStatus.OK))))

                .switchIfEmpty(
                        Mono.just(new ResponseEntity<>(responseError("Categoria no encontrada"), HttpStatus.NOT_FOUND))
                )

                .onErrorResume(error -> {
                    CategoryResponseRest response = responseError("Error al eliminar la categoria");
                    log.error(error.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response));
                });
    }

    private CategoryResponseRest responseSuccess(List<Category> categoryList, String msg) {
        CategoryResponseRest response = new CategoryResponseRest();
        response.getCategoryResponse().setCategoryList(categoryList);
        response.setMetadata("success", "00", msg);
        return response;
    }

    private CategoryResponseRest responseError(String msg) {
        CategoryResponseRest response = new CategoryResponseRest();
        response.setMetadata("error", "-1", msg);
        return response;
    }
}
