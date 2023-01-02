package com.caoc.inventory.model.documents;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@Document(collection = "categories")
public class Category implements Serializable {
    private static final long serialVersionUID = -6814063069421859263L;
    @Id
    private String id;
    private String name;
    private String description;

}
