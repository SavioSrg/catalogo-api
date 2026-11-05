package com.savio.catalogo_api.controller;

import com.savio.catalogo_api.dto.ProdutoRequest;
import com.savio.catalogo_api.dto.ProdutoResponse;
import com.savio.catalogo_api.service.ProdutoService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping
    public PagedModel<ProdutoResponse> listar(@RequestParam(required = false) Boolean ativo, Pageable pageable) {
        return new PagedModel<>(produtoService.listar(ativo, pageable));
    }

    @GetMapping("/{id}")
    public ProdutoResponse buscarPorId(@PathVariable long id) {
        return produtoService.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<ProdutoResponse> criar(@RequestBody ProdutoRequest request) {
        ProdutoResponse criado = produtoService.criar(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(criado.id())
                .toUri();
        return ResponseEntity.created(location).body(criado);
    }

    @PutMapping("/{id}")
    public ProdutoResponse atualizar(@PathVariable long id, @RequestBody ProdutoRequest request) {
        return produtoService.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable long id) {
        produtoService.remover(id);
    }


}
