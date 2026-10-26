package com.savio.catalogo_api.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    public static Connection getConnection() throws SQLException {
        String url = Config.url();
        String username = Config.usuario();
        String password = Config.senha();

        return DriverManager.getConnection(url, username, password);
    }
}
