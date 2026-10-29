package com.savio.catalogo_api.jdbc.domain;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public record Produto(
        Long id,
        String nome,
        String sku,
        String descricao,
        BigDecimal preco,
        boolean ativo,
        Long categoriaId,
        LocalDateTime criadoEm
) {

    public static Produto mapear(ResultSet rs) throws SQLException {
        return new Produto(
                rs.getLong("id"),
                rs.getString("nome"),
                rs.getString("sku"),
                rs.getString("descricao"),
                rs.getBigDecimal("preco"),
                rs.getBoolean("ativo"),
                rs.getLong("categoria_id"),
                rs.getObject("criado_em", LocalDateTime.class));
    }
}
