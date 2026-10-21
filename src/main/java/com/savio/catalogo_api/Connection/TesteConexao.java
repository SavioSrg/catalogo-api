package com.savio.catalogo_api.app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TesteConexao {

    public static void main(String[] args) throws SQLException {
        try (Connection conn = DriverManager.getConnection(
                Config.url(), Config.usuario(), Config.senha());
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("select version()")) {

            rs.next();
            System.out.println("Conectou: " + rs.getString(1));
        }
    }
}
