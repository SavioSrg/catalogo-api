package com.savio.catalogo_api.app;

import com.savio.catalogo_api.connection.Config;
import com.savio.catalogo_api.dao.ProdutoDao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MainTeste {
    public static void main(String[] args) {

        ProdutoDao dao = new ProdutoDao();

        try (Connection conn = DriverManager.getConnection(
                Config.url(), Config.usuario(), Config.senha())) {

            dao.registrarSaida(conn, 1, 1, "Verdade esse bilhete");


        } catch (SQLException e) {
            System.err.println("Erro inesperado de SQL: " + e.getMessage() + " [State: " + e.getSQLState() + "]");
        }
    }
}