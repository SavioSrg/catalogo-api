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
@Transactional(readOnly = true)
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public Page<ProdutoResponse> listar(Boolean ativo, Pageable pageable) {
        Page<ProdutoEntity> pagina = (ativo == null)
                ? produtoRepository.findAll(pageable)
                : produtoRepository.findByAtivo(ativo, pageable);
        return pagina.map(ProdutoResponse::from);
    }

    public ProdutoResponse buscarPorId(long id) {
        return ProdutoResponse.from(buscarEntidade(id));
    }

    @Transactional
    public ProdutoResponse criar(ProdutoRequest request) {
        ProdutoEntity salvo = produtoRepository.save(request.toEntity());
        return ProdutoResponse.from(salvo);
    }

    @Transactional
    public ProdutoResponse atualizar(long id, ProdutoRequest request) {
        ProdutoEntity entity = buscarEntidade(id);
        entity.setNome(request.nome());
        entity.setSku(request.sku());
        entity.setDescricao(request.descricao());
        entity.setPreco(request.preco());
        entity.setCategoriaId(request.categoriaId());
        return ProdutoResponse.from(entity);
    }

    @Transactional
    public void remover(long id) {
        buscarEntidade(id).setAtivo(false);
    }

    private ProdutoEntity buscarEntidade(long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado: id " + id));
    }
}
