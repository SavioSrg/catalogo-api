package com.savio.catalogo_api.jdbc.start;

import com.savio.catalogo_api.jdbc.connection.ConnectionFactory;
import com.savio.catalogo_api.jdbc.dao.ProdutoDao;
import com.savio.catalogo_api.jdbc.domain.Produto;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {

        ProdutoDao dao = new ProdutoDao();

        try (Connection conn = ConnectionFactory.getConnection()) {

            // 1. Geração de um SKU dinâmico e único para esta execução
            String skuDinamico = "PER-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();

            System.out.println("=== 1. Testando Inserção com Sucesso ===");
            Produto novo = new Produto(null, "Hub USB", skuDinamico, "Hub USB 4 portas",
                    new BigDecimal("89.90"), true, 2L, null);

            long id = dao.inserir(conn, novo);
            System.out.println("Criado com ID: " + id);
            System.out.println("Buscado: " + dao.buscarPorId(conn, id).orElseThrow());

            // Listagem de ativos
            System.out.println("\n=== Listando Produtos Ativos ===");
            dao.listarAtivos(conn, 0, 5).forEach(System.out::println);


            // ==========================================
            // 2. TESTES DE ERRO (23505, 23503, 23514)
            // ==========================================

            // Teste 23505: SKU já existe (Tentando inserir com o mesmo skuDinamico)
            System.out.println("\n=== Teste 23505: SKU duplicado ===");
            try {
                Produto produtoDuplicado = new Produto(null, "Outro Hub", skuDinamico, "Desc",
                        new BigDecimal("50.00"), true, 2L, null);
                dao.inserir(conn, produtoDuplicado);
            } catch (SQLException e) {
                if ("23505".equals(e.getSQLState())) {
                    System.out.println("-> Sucesso no teste: SKU já existe (23505)");
                } else {
                    throw e;
                }
            }

            // Teste 23503: Categoria inexistente (Foreign Key Violation)
            System.out.println("\n=== Teste 23503: Categoria inexistente ===");
            try {
                String skuUnico2 = "PER-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
                Produto produtoCategoriaInvalida = new Produto(null, "Teclado", skuUnico2, "Desc",
                        new BigDecimal("150.00"), true, 9999L, null); // ID 9999 não deve existir
                dao.inserir(conn, produtoCategoriaInvalida);
            } catch (SQLException e) {
                if ("23503".equals(e.getSQLState())) {
                    System.out.println("-> Sucesso no teste: Categoria inexistente (23503)");
                } else {
                    throw e;
                }
            }

            // Teste 23514: Preço inválido (Check Constraint Violation)
            System.out.println("\n=== Teste 23514: Preço inválido ===");
            try {
                String skuUnico3 = "PER-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
                Produto produtoPrecoInvalido = new Produto(null, "Mouse", skuUnico3, "Desc",
                        new BigDecimal("-10.00"), true, 2L, null); // Preço negativo (exige regra no DB)
                dao.inserir(conn, produtoPrecoInvalido);
            } catch (SQLException e) {
                if ("23514".equals(e.getSQLState())) {
                    System.out.println("-> Sucesso no teste: Preço inválido (23514)");
                } else {
                    throw e;
                }
            }

            // Limpeza: Desativa o produto criado no começo
            dao.desativar(conn, id);
            System.out.println("\nProduto ID " + id + " desativado com sucesso.");

        } catch (SQLException e) {
            System.err.println("Erro inesperado de SQL: " + e.getMessage() + " [State: " + e.getSQLState() + "]");
        }
    }
}