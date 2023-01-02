package com.caoc.inventory.model.response;

import com.caoc.inventory.model.documents.Category;
import lombok.Data;

import java.util.List;

@Data
public class CategoryResponse {
    private List<Category> categoryList;
}
