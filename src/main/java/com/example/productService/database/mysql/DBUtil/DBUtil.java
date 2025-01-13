package com.example.productService.database.mysql.DBUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {

    private static Connection connection=null;

    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        if (connection != null && !connection.isClosed()) {
            return connection;
        } else {
            String driver = "com.mysql.cj.jdbc.Driver";
            String url = "jdbc:mysql://localhost:3306/ProductServiceDB";
            String user  = "root";
            String password="mysql";

            Class.forName(driver);
            connection = DriverManager.getConnection(url,user,password);


        }

        return connection;
    }
}
