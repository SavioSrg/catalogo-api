package com.savio.catalogo_api.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "produto")
public class ProdutoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String nome;
    private String sku;
    private String descricao;
    private BigDecimal preco;
    private boolean ativo;
    private long categoriaId;
    private LocalDateTime criadoEm;

    public ProdutoEntity() {
    }

    public long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getSku() {
        return sku;
    }

    public String getDescricao() {
        return descricao;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public long getCategoriaId() {
        return categoriaId;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }
}
