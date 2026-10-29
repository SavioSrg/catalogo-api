package com.savio.catalogo_api.controller;

import com.savio.catalogo_api.entity.ProdutoEntity;
import com.savio.catalogo_api.repository.ProdutoRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoRepository produtoRepository;

    public ProdutoController(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @GetMapping
    public List<ProdutoEntity> listar(Pageable pageable) {
        return produtoRepository.findAll(pageable).getContent();
    }

    @GetMapping("/ativos")
    public List<ProdutoEntity> listarAtivo(@RequestParam boolean ativos, Pageable pageable) {
        return produtoRepository.findByAtivo(ativos, pageable).getContent();
    }

    // Busca por ID retornando 200 (OK) ou 404 (Not Found)
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoEntity> buscarPorId(@PathVariable Long id) {
        return produtoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
