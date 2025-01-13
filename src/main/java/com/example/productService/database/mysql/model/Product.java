package com.example.productService.database.mysql.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Product {
    @Id
    String pID;
    String pName;
    int pQuantity;
    double pPrice;

    @Override
    public String toString() {
        return "Product{" +
                "pID='" + pID + '\'' +
                ", pName='" + pName + '\'' +
                ", pQuantity=" + pQuantity +
                ", pPrice=" + pPrice +
                '}';
    }
}
