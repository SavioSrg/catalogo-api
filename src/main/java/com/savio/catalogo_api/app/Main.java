package com.savio.catalogo_api.app;

import com.savio.catalogo_api.connection.Config;
import com.savio.catalogo_api.dao.ProdutoDao;
import com.savio.catalogo_api.model.Produto;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {

        ProdutoDao dao = new ProdutoDao();

        try (Connection conn = DriverManager.getConnection(
                Config.url(), Config.usuario(), Config.senha())) {

            Produto novo = new Produto(null, "Hub USB", "PER-099", null,
                    new BigDecimal("89.90"), true, 2L, null);

            long id = dao.inserir(conn, novo);
            System.out.println("Criado: " + dao.buscarPorId(conn, id).orElseThrow());
            dao.listarAtivos(conn, 0, 5).forEach(System.out::println);
            dao.desativar(conn, id);

            try {
                dao.inserir(conn, novo);
            } catch (SQLException e) {
                switch (e.getSQLState()) {
                    case "23505" -> System.out.println("SKU já existe");
                    case "23503" -> System.out.println("Categoria inexistente");
                    case "23514" -> System.out.println("Preço inválido");
                    default -> throw e;
                }
            }
        }

    }
}
