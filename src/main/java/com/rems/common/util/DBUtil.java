package com.rems.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBUtil {

    private static final Properties config = new Properties();

    static {
        try (InputStream input = DBUtil.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new RuntimeException("Unable to find config.properties");
            }
            config.load(input);
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException("Cannot load database configuration", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        String url = config.getProperty("db.url");
        String username = config.getProperty("db.username");
        String password = config.getProperty("db.password");
        return DriverManager.getConnection(url, username, password);
    }

    public static String getConfigProperty(String key) {
        return config.getProperty(key);
    }
}
