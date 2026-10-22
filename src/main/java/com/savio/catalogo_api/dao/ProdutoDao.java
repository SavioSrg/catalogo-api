package com.savio.catalogo_api.dao;

import com.savio.catalogo_api.model.Produto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProdutoDao {
    private static final String COLUNAS = "id, nome, sku, descricao, preco, ativo, categoria_id, criado_em";

    public long inserir(Connection conn, Produto p) throws SQLException {
        String sql = """
                insert into produto (nome, sku, descricao, preco, categoria_id)
                values (?, ?, ?, ?, ?)
                returning id
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.nome());
            ps.setString(2, p.sku());
            ps.setString(3, p.descricao());
            ps.setBigDecimal(4, p.preco());
            ps.setLong(5, p.categoriaId());
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) throw new SQLException("INSERT não retornou id");
                return rs.getLong(1);
            }
        }
    }

    public Optional<Produto> buscarPorId(Connection conn, long id) throws SQLException {
        String sql = "select " + COLUNAS + " from produto where id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(Produto.mapear(rs)) : Optional.empty();
            }
        }
    }

    public List<Produto> listarAtivos(Connection conn, int pagina, int tamanho) throws SQLException {
        if (pagina < 0 || tamanho < 1 || tamanho > 100) {
            throw new IllegalArgumentException("pagina >= 0 e tamanho entre 1 e 100");
        }

        String sql = "select " + COLUNAS + """
                 from produto
                where ativo = true
                order by nome, id
                limit ? offset ?
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, tamanho);
            ps.setInt(2, pagina * tamanho);        // página começa em 0

            try (ResultSet rs = ps.executeQuery()) {
                List<Produto> lista = new ArrayList<>();
                while (rs.next()) {
                    lista.add(Produto.mapear(rs));
                }
                return lista;
            }
        }
    }

    public boolean atualizar(Connection conn, Produto p) throws SQLException {
        String sql = """
                update produto
                   set nome = ?, descricao = ?, preco = ?, categoria_id = ?
                 where id = ?
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.nome());
            ps.setString(2, p.descricao());
            ps.setBigDecimal(3, p.preco());
            ps.setLong(4, p.categoriaId());
            ps.setLong(5, p.id());
            return ps.executeUpdate() == 1;
        }
    }

    public boolean desativar(Connection conn, long id) throws SQLException {
        String sql = "update produto set ativo = false where id = ? and ativo = true";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() == 1;
        }
    }



}
