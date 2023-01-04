package com.caoc.inventory.model.documents;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@Document(collection = "products")
public class Producto implements Serializable {

    private static final long serialVersionUID = 2395941178456995969L;

    @Id
    private String id;
    private String name;
    private double price;
    private int account;
    @DBRef(lazy = true)
    private Category category;
    private byte[] picture;
}
