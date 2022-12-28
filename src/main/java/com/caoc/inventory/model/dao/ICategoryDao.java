package com.caoc.inventory.model.dao;

import com.caoc.inventory.model.documents.Category;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@EnableReactiveMongoRepositories
public interface ICategoryDao extends ReactiveMongoRepository<Category, String> {

}
