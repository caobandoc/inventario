package com.caoc.inventory.model.dao;

import com.caoc.inventory.model.documents.Producto;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import reactor.core.publisher.Flux;

@EnableReactiveMongoRepositories
public interface IProductDao extends ReactiveMongoRepository<Producto, String> {
    Flux<Producto> findByNameContainingIgnoreCase(String name);

}
