package com.addressbook.config;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConfig {
    private static final Properties properties = new Properties();
    static {
        try{
            InputStream input =
                    DatabaseConfig.class.getClassLoader()
                            .getResourceAsStream("db.properties");

            if (input == null){
                throw new RuntimeException("db.prperties not found !");
            }
            properties.load(input);
            Class.forName("org.postgresql.Driver");

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                properties.getProperty("db.url"),
                properties.getProperty("db.username"),
                properties.getProperty("db.password")
        );
    }

    public static void main(String[] args) {
        try (Connection connection = getConnection()){
            System.out.println("Connection Successful !");
            System.out.println(connection != null);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
