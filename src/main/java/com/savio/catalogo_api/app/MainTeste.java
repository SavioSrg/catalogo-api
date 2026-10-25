package com.savio.catalogo_api.app;

import com.savio.catalogo_api.connection.Config;
import com.savio.catalogo_api.dao.ProdutoDao;
import com.savio.catalogo_api.model.Produto;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class MainTeste {
    public static void main(String[] args) {

        ProdutoDao dao = new ProdutoDao();

        try (Connection conn = DriverManager.getConnection(
                Config.url(), Config.usuario(), Config.senha())) {

//            dao.registrarSaida(conn, 1, 1, "Verdade esse bilhete");

            List<Produto> listaProdutosEncontrados = dao.buscarPorNome(conn, "ca");
            listaProdutosEncontrados.stream()
                    .map(produto -> produto.nome() + " - " + produto.preco())
                    .forEach(System.out::println);

        } catch (SQLException e) {
            System.err.println("Erro inesperado de SQL: " + e.getMessage() + " [State: " + e.getSQLState() + "]");
        }
    }
}