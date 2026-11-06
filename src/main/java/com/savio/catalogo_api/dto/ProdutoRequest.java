package com.savio.catalogo_api.dto;

import com.savio.catalogo_api.entity.ProdutoEntity;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProdutoRequest(
        @NotBlank @Size(max = 150) String nome,
        @NotBlank @Size(max = 50) String sku,
        @Size(max = 180) String descricao,
        @NotNull @PositiveOrZero @Digits(integer = 8, fraction = 2) BigDecimal preco,
        @Positive long categoriaId
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
