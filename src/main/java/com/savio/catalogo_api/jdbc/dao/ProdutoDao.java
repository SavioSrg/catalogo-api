package com.savio.catalogo_api.jdbc.dao;

import com.savio.catalogo_api.jdbc.domain.Produto;
import com.savio.catalogo_api.model.SaidaMensal;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    // buscarPorNome encontra o que contenham a String buscada
    public List<Produto> buscarPorNome(Connection conn, String nome) throws SQLException {
        String sql = "select " + COLUNAS + " from produto where nome ilike(?) order by nome, id";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, String.format("%%%s%%", nome));

            try (ResultSet rs = ps.executeQuery()) {
                List<Produto> lista = new ArrayList<>();
                while (rs.next()) {
                    lista.add(Produto.mapear(rs));
                }
                return lista;
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

    public void registrarSaida(Connection conn, long produtoId, int quantidade, String observacao) throws SQLException {
        if (quantidade <= 0) throw new IllegalArgumentException("Quantidade inválida");

        conn.setAutoCommit(false);

        try {
            // 1. trava a linha do produto: saídas simultâneas esperam a vez
            try (PreparedStatement ps = conn.prepareStatement(
                    "select id from produto where id = ? and ativo = true for update")) {
                ps.setLong(1, produtoId);

                try (ResultSet rs = ps.executeQuery()) {
                    // 1.1. Se o produto estiver inexistente ou inativo e lança uma exceção
                    if (!rs.next()) {
                        throw new IllegalArgumentException("Produto inexistente ou inativo");
                    }
                }
            }

            // 2. calcula o saldo atual
            int saldo;

            String sqlSaldo = """
                    select
                        coalesce(
                            sum(
                                case tipo when 'ENTRADA' then quantidade else -quantidade end
                                )
                            , 0)
                            from movimentacao_estoque where produto_id = ?
                    """;
            try (PreparedStatement ps = conn.prepareStatement(sqlSaldo)) {
                ps.setLong(1, produtoId);

                try (ResultSet rs = ps.executeQuery()) {
                    rs.next();
                    saldo = rs.getInt(1);

                    // 2.1. Verificar a quantidade do estoque
                    if (saldo < quantidade) {
                        throw new IllegalArgumentException("Estoque insuficiente: saldo " + saldo);
                    }
                }
            }
            // 3. grava a saída

            String sqlSaida = """
                    insert into movimentacao_estoque (produto_id, tipo, quantidade, observacao)
                    values (?, 'SAIDA', ?, ?)
                    """;
            try (PreparedStatement ps = conn.prepareStatement(sqlSaida)) {
                ps.setLong(1, produtoId);
                ps.setInt(2, quantidade);
                ps.setString(3, observacao);
                ps.executeUpdate();
            }
            conn.commit();
        } catch (SQLException | RuntimeException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    public List<Produto> buscarProdutoSemMovimentacao(Connection conn) throws SQLException {
        String sql = """
                   select id, nome, sku, descricao, preco, ativo, categoria_id, criado_em
                   from produto p
                   where not exists (
                       select 1 from movimentacao_estoque me where me.produto_id = p.id
                   )
                """;

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            List<Produto> lista = new ArrayList<>();

            while (rs.next()) {
                lista.add(Produto.mapear(rs));
            }
            return lista;
        }
    }

    public List<SaidaMensal> totalSaidaMes(Connection conn) throws SQLException {
        String sql = """
                select
                	date_trunc('month', me.data_movimentacao ) as mes,
                	SUM(me.quantidade) as total
                from produto p
                inner join movimentacao_estoque me on p.id = me.produto_id
                	and me.tipo = 'SAIDA'
                group by date_trunc('month', me.data_movimentacao)
                ORDER BY mes;
                """;
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            List<SaidaMensal> lista = new ArrayList<>();
            while (rs.next()) {
                lista.add(new SaidaMensal(
                        LocalDate.from(rs.getObject("mes", LocalDateTime.class)),
                        rs.getLong("total")));
            }
            return lista;
        }
    }
}
