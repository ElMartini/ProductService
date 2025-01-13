package com.example.productService.database.mysql.service;

import com.example.productService.database.mysql.DBUtil.DBUtil;
import com.example.productService.database.mysql.model.ActionStatus;
import com.example.productService.database.mysql.model.Product;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    ActionStatusServiceImpl actionStatusService = new ActionStatusServiceImpl();

    @Autowired
    static List<Product> productList = new ArrayList<>();


    Connection connection;

    public ProductServiceImpl() throws SQLException, ClassNotFoundException {
        connection = DBUtil.getConnection();
    }


    @Override
    public List<Product> getProductData() throws SQLException, ClassNotFoundException {
        connection = DBUtil.getConnection();

        productList.clear();
        String statementQuery = "SELECT * FROM product";
        PreparedStatement statement = connection.prepareStatement(statementQuery);

        ResultSet rs = statement.executeQuery();

        while (rs.next()) {
            Product product = new Product();
            product.setPID(rs.getString(1));
            product.setPName(rs.getString(2));
            product.setPPrice(rs.getDouble(3));
            product.setPQuantity(rs.getInt(4));
            productList.add(product);
        }
        return productList;


    }


    private ActionStatus add(Product product) throws SQLException, ClassNotFoundException {
        String statementQuery = "INSERT INTO product (pid, pName, pPrice, pQuantity) VALUES (?, ?, ?, ?)";
        connection = DBUtil.getConnection();
        PreparedStatement statement = connection.prepareStatement(statementQuery);

        if (product.getPID() == null || product.getPID().isEmpty()) {
            product.setPID(UUID.randomUUID().toString());
        }

        statement.setString(1, product.getPID());
        statement.setString(2, product.getPName());
        statement.setDouble(3, product.getPPrice());
        statement.setInt(4, product.getPQuantity());
        int i = statement.executeUpdate();

        ActionStatus actionStatus = new ActionStatus();
        actionStatus.setQuery("INSERT INTO product (pid, pName, pPrice, pQuantity) VALUES ("
                + product.getPID() + ", "
                + product.getPName() + ", "
                + product.getPPrice() + ", "
                + product.getPQuantity() + ")");
        if (i < 1) {
            actionStatus.setStatus("FAILED");
        } else {
            actionStatus.setStatus("SUCCESS");
        }
        return actionStatus;
    }

    @Override
    public boolean addProduct(Product product) throws SQLException, ClassNotFoundException {
        ActionStatus actionStatus = new ActionStatus();
        Product searchProduct = searchProduct(product.getPName());
        if (searchProduct != null) {
            product.setPQuantity(product.getPQuantity() + searchProduct.getPQuantity());
            actionStatus = changeQuantity(product, searchProduct.getPID());
        } else {

            actionStatus = add(product);
        }

        int result = actionStatusService.addActionStatus(actionStatus);

        if (actionStatus.getStatus().equals("SUCCESS") && result > 0) {
            return true;
        } else {
            return false;
        }

    }

    private ActionStatus delete(String pname) throws SQLException, ClassNotFoundException {
        connection = DBUtil.getConnection();
        String deleteQuery = "DELETE FROM product Where pname = ?";
        PreparedStatement statement = connection.prepareStatement(deleteQuery);
        statement.setString(1, pname);
        int i = statement.executeUpdate();

        ActionStatus actionStatus = new ActionStatus();
        actionStatus.setQuery("DELETE FROM product Where pname = " + pname);
        if (i < 1) {
            actionStatus.setStatus("FAILED");
        } else {
            actionStatus.setStatus("SUCCESS");
        }
        return actionStatus;

    }

    @Override
    public boolean deleteProduct(String pname) throws SQLException, ClassNotFoundException {
        if (searchProduct(pname) != null) {
            ActionStatus actionStatus = delete(pname);
            int result = actionStatusService.addActionStatus(actionStatus);
            if (actionStatus.getStatus().equals("SUCCESS") && result > 0) {
                return true;
            } else {
                return false;
            }

        } else {
            log.info("Nie ma podanego produktu w bazie");
            return true;
        }

    }

    private ActionStatus update(Product product, String pid) throws SQLException, ClassNotFoundException {
        connection = DBUtil.getConnection();

        String pName = product.getPName();
        double pPrice = product.getPPrice();
        int pQuantity = product.getPQuantity();

        String statementQuery = "UPDATE product SET pName = ?, pPrice = ?, pQuantity = ? WHERE pid = ?";
        PreparedStatement statement = connection.prepareStatement(statementQuery);
        statement.setString(1, pName);
        statement.setDouble(2, pPrice);
        statement.setInt(3, pQuantity);
        statement.setString(4, pid);
        int i = statement.executeUpdate();

        ActionStatus actionStatus = new ActionStatus();
        actionStatus.setQuery("UPDATE product SET pName = " + pName + ", pPrice = " + pPrice + ", pQuantity = " + pQuantity + " WHERE pid = " + pid);

        if (i < 1) {
            actionStatus.setStatus("FAILED");
        } else {
            actionStatus.setStatus("SUCCESS");
        }
        return actionStatus;


    }

    @Override
    public boolean updateProduct(Product product, String pid) throws SQLException, ClassNotFoundException {
        if (searchProduct(product.getPName()) != null) {
            ActionStatus actionStatus = update(product, pid);
            int result = actionStatusService.addActionStatus(actionStatus);
            if (actionStatus.getStatus().equals("SUCCESS") && result > 0) {
                return true;
            } else {
                return false;
            }

        } else {
            log.info("Nie ma podanego produktu w bazie");
            return true;
        }
    }

    private ActionStatus changeQuantity(Product product, String pid) throws SQLException, ClassNotFoundException {
        if (searchProduct(product.getPName()) != null) {
            ActionStatus actionStatus = update(product, pid);
            int result = actionStatusService.addActionStatus(actionStatus);
            if (actionStatus.getStatus().equals("SUCCESS") && result > 0) {
                return actionStatus;
            } else {
                return null;
            }

        }
        return null;
    }

    public boolean isProductQuantityInStock(String pName, int pQuantity) throws SQLException, ClassNotFoundException {
        Product searchedProduct = searchProduct(pName);
        if (searchedProduct == null) return false;
        else {
            if (searchedProduct.getPQuantity() >= pQuantity) return true;
            else return false;
        }
    }


    private Product searchProduct(String pName) throws SQLException, ClassNotFoundException {
        getProductData();
        for (Product p : productList) {
            if (p.getPName().equals(pName)) {
                return p;
            }
        }
        return null;
    }


}
