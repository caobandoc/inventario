package com.caoc.inventory.model.response;
import com.caoc.inventory.model.documents.Producto;
import lombok.Data;

import java.util.List;

@Data
public class ProductResponse {

    List<Producto> products;
}
