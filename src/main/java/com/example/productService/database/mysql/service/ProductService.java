package com.example.productService.database.mysql.service;

import com.example.productService.database.mysql.model.Product;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

@Repository
public interface ProductService {
    List<Product> getProductData() throws SQLException, ClassNotFoundException;
    boolean addProduct(Product product) throws SQLException, ClassNotFoundException;
    boolean deleteProduct(String pname) throws SQLException, ClassNotFoundException;
    boolean updateProduct(Product product, String pid) throws SQLException, ClassNotFoundException;
    boolean isProductQuantityInStock(String pName, int pQuantity) throws SQLException, ClassNotFoundException;

}
