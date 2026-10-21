package com.savio.catalogo_api.Connection;

import com.savio.catalogo_api.model.Produto;

import java.math.BigDecimal;
import java.sql.*;
import java.util.Optional;

public class TesteConexao {

    public static void main(String[] args) throws SQLException {
        try (Connection conn = DriverManager.getConnection(
                Config.url(), Config.usuario(), Config.senha());
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("select * from produto")) {

            if (rs.next()) {
                System.out.println("Conectou com sucesso! Primeira linha encontrada: " + rs.getString("nome"));
            }

            // --- CREATE (Inserção) ---
            String sqlInsert = """
                    insert into produto (nome, sku, descricao, preco, categoria_id)
                    values (?, ?, ?, ?, ?)
                    returning id
                    """;

            String nome = "Notebook Gamer";
            String sku = "NOTE-GAMER-01";
            String descricao = "Processador de última geração e placa dedicada";
            BigDecimal preco = new BigDecimal("4500.00");
            Long categoriaId = 1L;

            long idGerado = 0;
            try (PreparedStatement ps = conn.prepareStatement(sqlInsert)) {
                ps.setString(1, nome);
                ps.setString(2, sku);
                ps.setString(3, descricao);
                ps.setBigDecimal(4, preco);
                ps.setLong(5, categoriaId);

                try (ResultSet rs1 = ps.executeQuery()) {
                    if (rs1.next()) {
                        idGerado = rs1.getLong("id");
                        System.out.println("Produto inserido com sucesso! Novo ID gerado: " + idGerado);
                    }
                }
            } catch (SQLException e) {
                System.out.println("Erro ao inserir produto: " + e.getMessage());
            }

            // --- READ (Consulta) ---
            String sqlConsulta = "select * from produto where id = ?";
            long idParaBuscar = idGerado > 0 ? idGerado : 13L;

            try (PreparedStatement ps = conn.prepareStatement(sqlConsulta)) {
                ps.setLong(1, idParaBuscar);
                try (ResultSet rs2 = ps.executeQuery()) {
                    Optional<Produto> produtoOpt = rs2.next() ? Optional.of(Produto.mapear(rs2)) : Optional.empty();

                    if (produtoOpt.isPresent()) {
                        System.out.println("Produto encontrado no READ: " + produtoOpt.get().nome());
                    }
                }
            }

            // --- UPDATE (Atualização) ---
            String sqlUpdate = """
                    update produto 
                    set nome = ?, descricao = ?, preco = ? 
                    where id = ?
                    """;

            // Novos valores para atualizar
            String novoNome = "Notebook Gamer Pro";
            String novaDescricao = "Versão atualizada com 32GB de RAM";
            BigDecimal novoPreco = new BigDecimal("5200.00");

            try (PreparedStatement ps = conn.prepareStatement(sqlUpdate)) {
                ps.setString(1, novoNome);
                ps.setString(2, novaDescricao);
                ps.setBigDecimal(3, novoPreco);
                ps.setLong(4, idParaBuscar); // O ID do produto que você quer alterar

                int linhasAfetadas = ps.executeUpdate();

                if (linhasAfetadas > 0) {
                    System.out.println("\nProduto atualizado com sucesso! Linhas modificadas: " + linhasAfetadas);
                } else {
                    System.out.println("\nNenhum produto foi atualizado. O ID " + idParaBuscar + " não existe.");
                }

            } catch (SQLException e) {
                System.out.println("Erro ao atualizar produto: " + e.getMessage());
            }

            // --- SOFT DELETE (Desativação lógica) ---
            String sqlSoftDelete = "update produto set ativo = false where id = ? and ativo = true";

            try (PreparedStatement ps = conn.prepareStatement(sqlSoftDelete)) {
                ps.setLong(1, idParaBuscar); // O ID do produto que você quer "apagar"

                int linhasAfetadas = ps.executeUpdate();

                if (linhasAfetadas > 0) {
                    System.out.println("\nProduto desativado com sucesso (Soft Delete)!");
                } else {
                    System.out.println("\nNenhum produto foi desativado. Ou o ID " + idParaBuscar + " não existe, ou ele já estava inativo.");
                }

            } catch (SQLException e) {
                System.out.println("Erro ao desativar produto: " + e.getMessage());
            }
        }
    }
}