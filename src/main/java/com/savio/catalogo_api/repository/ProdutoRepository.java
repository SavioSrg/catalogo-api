package com.savio.catalogo_api.repository;

import com.savio.catalogo_api.entity.ProdutoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository extends JpaRepository<ProdutoEntity, Long> {

    Page<ProdutoEntity> findByAtivo(boolean ativo, Pageable pageable);
}
