package com.savio.catalogo_api.dto;

import java.math.BigDecimal;

public record ProdutoRequest(
        String nome,
        String sku,
        String descricao,
        BigDecimal preco,
        long categoriaId
) {
}
