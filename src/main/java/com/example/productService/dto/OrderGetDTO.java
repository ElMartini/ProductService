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
public class OrderGetDTO implements Serializable {
    @JsonProperty("oID")
    private String oID;
    @JsonProperty("pNames")
    private List<String> pNames;
    @JsonProperty("cID")
    private String cID;

}
