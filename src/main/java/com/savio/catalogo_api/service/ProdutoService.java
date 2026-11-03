package com.savio.catalogo_api.service;

import com.savio.catalogo_api.dto.ProdutoResponse;
import com.savio.catalogo_api.entity.ProdutoEntity;
import com.savio.catalogo_api.exception.ResourceNotFoundException;
import com.savio.catalogo_api.repository.ProdutoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProdutoService {
    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @Transactional(readOnly = true)
    public ProdutoResponse buscarPorId(long id){
        ProdutoEntity entity = produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

    return  ProdutoResponse.from(entity);
    }

    public Page<ProdutoResponse> listar(Pageable pageable) {
        return produtoRepository.findAll(pageable).map(ProdutoResponse::from);
    }

    public Page<ProdutoResponse> listarPorAtivo(boolean ativo, Pageable pageable) {
        return produtoRepository.findByAtivo(ativo, pageable).map(ProdutoResponse::from);
    }
}
