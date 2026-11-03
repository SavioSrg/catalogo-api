package com.savio.catalogo_api.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

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

    @Column(name = "criado_em", updatable = false)
    private LocalDateTime criadoEm;


    @PrePersist
    public void prePersist() {
        this.criadoEm = LocalDateTime.now();
    }

    public ProdutoEntity() {
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public BigDecimal getPreco() { return preco; }
    public void setPreco(BigDecimal preco) { this.preco = preco; }

    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }

    public long getCategoriaId() { return categoriaId; }
    public void setCategoriaId(long categoriaId) { this.categoriaId = categoriaId; }

    public LocalDateTime getCriadoEm() { return criadoEm; }
}
