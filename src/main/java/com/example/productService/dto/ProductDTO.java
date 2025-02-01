package com.example.productService.dto;

import com.example.productService.database.mysql.model.Product;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDTO implements Serializable {
    @JsonProperty("action")
    private String action;
    @JsonProperty("product")
    private Product product;
}
