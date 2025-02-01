package com.example.productService.dto;

import com.example.productService.database.mysql.model.Product;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ToOrderBasketDTO implements Serializable {
    @JsonProperty("products")
    private List<Product> products;
    @JsonProperty("cID")
    private String cID;

    public String getcID() {
        return cID;
    }

    public void setcID(String cID) {
        this.cID = cID;
    }
}
