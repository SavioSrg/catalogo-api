package com.savio.catalogo_api.controller;

import com.savio.catalogo_api.dto.ProdutoResponse;
import com.savio.catalogo_api.entity.ProdutoEntity;
import com.savio.catalogo_api.repository.ProdutoRepository;
import com.savio.catalogo_api.service.ProdutoService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping
    public List<ProdutoResponse> listar(Pageable pageable) {
        return produtoService.listar(pageable).getContent();
    }

    @GetMapping("/ativos")
    public List<ProdutoResponse> listarAtivo(@RequestParam boolean ativos, Pageable pageable) {
        return produtoService.listarPorAtivo(ativos, pageable).getContent();
    }

    @GetMapping("/{id}")
    public ProdutoResponse buscarPorId(@PathVariable long id) {
        return produtoService.buscarPorId(id);
    }
}
