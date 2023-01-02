package com.caoc.inventory.model.dao;

import com.caoc.inventory.model.documents.Producto;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@EnableReactiveMongoRepositories
public interface IProductDao extends ReactiveMongoRepository<Producto, String> {

}
