package com.savio.catalogo_api.jdbc.connection;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionFactory {

    private static final HikariDataSource DATA_SOURCE;

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(Config.url());
        config.setUsername(Config.usuario());
        config.setPassword(Config.senha());
        config.setMaximumPoolSize(20);

        DATA_SOURCE = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        return DATA_SOURCE.getConnection();
    }

    //    public static Connection getConnection() throws SQLException {
    //        String url = Config.url();
    //        String username = Config.usuario();
    //        String password = Config.senha();
    //
    //        return DriverManager.getConnection(url, username, password);
    //    }
}
