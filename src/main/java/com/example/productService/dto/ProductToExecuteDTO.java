package com.example.productService.dto;

import com.example.productService.database.mysql.model.Product;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductToExecuteDTO implements Serializable {

    @JsonProperty("products")
    private List<Product> products;

    @JsonProperty("cID")
    private String cID;

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }


    public String getcID() {
        return cID;
    }

    public void setcID(String cID) {
        this.cID = cID;
    }
}
