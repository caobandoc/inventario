package com.caoc.inventory.services.impl;

import com.caoc.inventory.model.dao.ICategoryDao;
import com.caoc.inventory.model.dao.IProductDao;
import com.caoc.inventory.model.documents.Producto;
import com.caoc.inventory.model.response.ProductResponseRest;
import com.caoc.inventory.services.IProductService;
import com.caoc.inventory.util.Util;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ProductServiceImpl implements IProductService {

    private IProductDao productDao;
    private ICategoryDao categoryDao;

    @Override
    @Transactional(readOnly = true)
    public Mono<ResponseEntity<ProductResponseRest>> search() {
        return productDao.findAll()
                .map(product -> {
                    product.setImage(Util.decompressZLib(product.getImage()));
                    return product;
                })
                .collectList()
                .flatMap(products -> {
                    ProductResponseRest response = responseSuccess(products, "Productos encontrados");
                    return Mono.just(ResponseEntity.status(HttpStatus.OK).body(response));
                })
                .onErrorResume(error -> {
                    ProductResponseRest response = responseError("Error al buscar los productos");
                    log.error(error.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response));
                });
    }

    @Override
    @Transactional
    public Mono<ResponseEntity<ProductResponseRest>> save(Producto producto, String idCategory) {
        return categoryDao.findById(idCategory)
                .map(category -> {
                    producto.setCategory(category);
                    return producto;
                })
                .flatMap(productDao::save)
                .flatMap(product -> {
                    ProductResponseRest response = responseSuccess(Arrays.asList(product), "Producto guardado");
                    return Mono.just(ResponseEntity.status(HttpStatus.CREATED).body(response));
                })
                .switchIfEmpty(
                        Mono.just(new ResponseEntity<>(responseError("Categoria no encontrada"), HttpStatus.NOT_FOUND))
                )
                .onErrorResume(error -> {
                    ProductResponseRest response = responseError("Error al guardar el producto");
                    log.error(error.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response));
                });

    }

    @Override
    @Transactional(readOnly = true)
    public Mono<ResponseEntity<ProductResponseRest>> searchById(String id) {
        return productDao.findById(id)
                .flatMap(product -> {
                    product.setImage(Util.decompressZLib(product.getImage()));
                    ProductResponseRest response = responseSuccess(Arrays.asList(product), "Producto encontrado");
                    return Mono.just(ResponseEntity.status(HttpStatus.OK).body(response));
                })
                .switchIfEmpty(
                        Mono.just(new ResponseEntity<>(responseError("Producto no encontrado"), HttpStatus.NOT_FOUND))
                )
                .onErrorResume(error -> {
                    ProductResponseRest response = responseError("Error al buscar el producto");
                    log.error(error.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response));
                });
    }

    @Override
    @Transactional
    public Mono<ResponseEntity<ProductResponseRest>> searchByName(String name) {
        return productDao.findByNameContainingIgnoreCase(name)
                .map(product -> {
                    product.setImage(Util.decompressZLib(product.getImage()));
                    return product;
                })
                .collectList()
                .flatMap(products -> {
                    ProductResponseRest response = responseSuccess(products, "Productos encontrados");
                    return Mono.just(ResponseEntity.status(HttpStatus.OK).body(response));
                })
                .switchIfEmpty(
                        Mono.just(new ResponseEntity<>(responseError("Productos no encontrados"), HttpStatus.NOT_FOUND))
                )
                .onErrorResume(error -> {
                    ProductResponseRest response = responseError("Error al buscar los productos");
                    log.error(error.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response));
                });
    }

    @Override
    @Transactional
    public Mono<ResponseEntity<ProductResponseRest>> deleteById(String id) {
        return productDao.findById(id)
                .flatMap(product -> {
                    productDao.deleteById(id).subscribe();
                    ProductResponseRest response = responseSuccess(Arrays.asList(product), "Producto eliminado");
                    return Mono.just(ResponseEntity.status(HttpStatus.OK).body(response));
                })
                .switchIfEmpty(
                        Mono.just(new ResponseEntity<>(responseError("Producto no encontrado"), HttpStatus.NOT_FOUND))
                )
                .onErrorResume(error -> {
                    ProductResponseRest response = responseError("Error al eliminar el producto");
                    log.error(error.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response));
                });
    }

    private ProductResponseRest responseSuccess(List<Producto> productoList, String msg) {
        ProductResponseRest response = new ProductResponseRest();
        response.getProductResponse().setProducts(productoList);
        response.setMetadata("success", "00", msg);
        return response;
    }

    private ProductResponseRest responseError(String msg) {
        ProductResponseRest response = new ProductResponseRest();
        response.setMetadata("error", "-1", msg);
        return response;
    }
}
