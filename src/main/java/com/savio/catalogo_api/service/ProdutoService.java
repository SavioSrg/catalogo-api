package com.savio.catalogo_api.service;

import com.savio.catalogo_api.dto.ProdutoRequest;
import com.savio.catalogo_api.dto.ProdutoResponse;
import com.savio.catalogo_api.entity.ProdutoEntity;
import com.savio.catalogo_api.exception.ResourceNotFoundException;
import com.savio.catalogo_api.repository.ProdutoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProdutoService {
    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @Transactional(readOnly = true)
    public ProdutoResponse buscarPorId(long id) {
        ProdutoEntity entity = produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

        return ProdutoResponse.from(entity);
    }

    public Page<ProdutoResponse> listar(Pageable pageable) {
        return produtoRepository.findAll(pageable).map(ProdutoResponse::from);
    }

    public Page<ProdutoResponse> listarPorAtivo(boolean ativo, Pageable pageable) {
        return produtoRepository.findByAtivo(ativo, pageable).map(ProdutoResponse::from);
    }

    public ProdutoResponse criar(ProdutoRequest request) {
        // 1. Converte o request (DTO) para a entidade do banco
        ProdutoEntity produtoEntity = request.toEntity();

        // 2. Salva no banco de dados (o @PrePersist roda aqui, gerando o criado_em)
        ProdutoEntity produtoSalvo = produtoRepository.save(produtoEntity);

        // 3. Converte a entidade salva de volta para o response (DTO de saída) e a retorna
        return ProdutoResponse.from(produtoSalvo);
    }
}
