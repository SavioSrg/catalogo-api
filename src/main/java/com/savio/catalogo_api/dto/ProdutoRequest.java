package com.savio.catalogo_api.dto;

import com.savio.catalogo_api.entity.ProdutoEntity;

import java.math.BigDecimal;

public record ProdutoRequest(
        String nome,
        String sku,
        String descricao,
        BigDecimal preco,
        long categoriaId
) {

    public ProdutoEntity toEntity() {
        ProdutoEntity produto = new ProdutoEntity();
        produto.setNome(this.nome);
        produto.setSku(this.sku);
        produto.setDescricao(this.descricao);
        produto.setPreco(this.preco);
        produto.setAtivo(true);
        produto.setCategoriaId(this.categoriaId);

        return produto;
    }
}
