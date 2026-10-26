package com.savio.catalogo_api.app;

import com.savio.catalogo_api.connection.ConnectionFactory;
import com.savio.catalogo_api.dao.ProdutoDao;
import com.savio.catalogo_api.model.Produto;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class MainTeste {
    public static void main(String[] args) {

        ProdutoDao dao = new ProdutoDao();

        try (Connection conn = ConnectionFactory.getConnection()) {
            System.out.println("Produtos com 'hub'");
            // teste com a String "hub"
            List<Produto> listaProdutosEncontradosTeste1 = dao.buscarPorNome(conn, "hub");
            listaProdutosEncontradosTeste1.stream()
                    .map(produto -> produto.nome() + " - " + produto.preco())
                    .forEach(System.out::println);

            System.out.println("\nProdutos com 'zzz'");
//            // teste com a String "zzz"
//            List<Produto> listaProdutosEncontradosTeste2 = dao.buscarPorNome(conn, "zzz");
//            listaProdutosEncontradosTeste2.stream()
//                    .map(produto -> produto.nome() + " - " + produto.preco())
//                    .forEach(System.out::println);

            System.out.println("\nProdutos com '%'");
            // teste com a ‘String’ "%"
            List<Produto> listaProdutosEncontradosTeste3 = dao.buscarPorNome(conn, "%");
            listaProdutosEncontradosTeste3.stream()
                    .map(produto -> produto.nome() + " - " + produto.preco())
                    .forEach(System.out::println);

            // Teste 1 - sucesso
            dao.registrarSaida(conn, 5, 5, "Teste do produto mouse");

            // Teste 2 - IllegalArgumentException
            dao.registrarSaida(conn, 5, 100, "Teste do produto mouse");

            // Teste 3 - IllegalArgumentException
            dao.registrarSaida(conn, 5, 0, "Teste do produto mouse");

            // Teste 4 - IllegalArgumentException
            dao.registrarSaida(conn, 999, 100, "Teste do produto mouse");

        } catch (SQLException e) {
            System.err.println("Erro inesperado de SQL: " + e.getMessage() + " [State: " + e.getSQLState() + "]");
        }
    }
}