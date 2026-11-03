package com.savio.catalogo_api.controller;

import com.savio.catalogo_api.dto.ProdutoRequest;
import com.savio.catalogo_api.dto.ProdutoResponse;
import com.savio.catalogo_api.entity.ProdutoEntity;
import com.savio.catalogo_api.repository.ProdutoRepository;
import com.savio.catalogo_api.service.ProdutoService;
import org.springframework.data.domain.Pageable;
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

    @PostMapping
    public ResponseEntity<ProdutoResponse> criar(@RequestBody ProdutoRequest request) {
        // 1. Chama o service para salvar e receber o response preenchido
        ProdutoResponse produtoCriado = produtoService.criar(request);

        // 2. Constrói a URL do novo recurso criado para colocar no header Location
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(produtoCriado.id()) // Supondo que o record tenha o campo id()
                .toUri();

        // 3. Retorna o status 201 Created, com o header Location e o corpo com os dados
        return ResponseEntity.created(location).body(produtoCriado);
    }
}
