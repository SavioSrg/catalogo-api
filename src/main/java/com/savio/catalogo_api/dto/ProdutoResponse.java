package com.savio.catalogo_api.dto;

import com.savio.catalogo_api.entity.ProdutoEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProdutoResponse(
        long id,
        String nome,
        String sku,
        String descricao,
        BigDecimal preco,
        boolean ativo,
        long categoriaId,
        LocalDateTime criadoEm
) {

    public static ProdutoResponse from(ProdutoEntity entity) {
        return new ProdutoResponse(
                entity.getId(),
                entity.getNome(),
                entity.getSku(),
                entity.getDescricao(),
                entity.getPreco(),
                entity.isAtivo(),
                entity.getCategoriaId(),
                entity.getCriadoEm()
        );
    }
}
