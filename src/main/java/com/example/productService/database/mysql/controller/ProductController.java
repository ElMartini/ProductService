package com.example.productService.database.mysql.controller;

import com.example.productService.database.mysql.model.Product;
import com.example.productService.database.mysql.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/get")
    public List<Product> getPersons() {
        try {
            return this.productService.getProductData();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping(value = "/add")
    public void addProduct(@RequestBody Product product) throws InterruptedException {

        boolean isSuccess;
        do {
            try {
                isSuccess = productService.addProduct(product);
            } catch (SQLException | ClassNotFoundException e) {
                isSuccess = false;
                log.info(e.toString());
            }
            if (!isSuccess)
                Thread.sleep(500);

        } while (!isSuccess);
        log.info("Success");
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/delete/{pname}")
    public void deleteProduct(@PathVariable String pname) throws InterruptedException {
        pname = pname.trim();
        boolean isSuccess;
        do {
            try {
                isSuccess = productService.deleteProduct(pname);
            } catch (SQLException | ClassNotFoundException e) {
                isSuccess = false;
                log.info(e.toString());
            }
            if (!isSuccess)
                Thread.sleep(500);
        } while (!isSuccess);
        log.info("SUCCESS");
    }

    @PutMapping(value = "/update")
    public void updateProduct(@RequestBody Product product) throws InterruptedException {

        boolean isSuccess;
        do {
            try {
                isSuccess = productService.updateProduct(product, product.getPID());
            } catch (SQLException | ClassNotFoundException e) {
                isSuccess = false;
                log.info(e.toString());
            }
            if (!isSuccess)
                Thread.sleep(500);

        } while (!isSuccess);
        log.info("Success1");
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public boolean isInStock(@RequestParam String pName, @RequestParam int pQuantity){
        try {
            return productService.isProductQuantityInStock(pName,pQuantity);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


}
