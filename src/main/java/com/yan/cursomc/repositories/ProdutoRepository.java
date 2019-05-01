package com.yan.cursomc.repositories;

import java.util.List;

import com.yan.cursomc.domain.Categoria;
import com.yan.cursomc.domain.Produto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;


public interface ProdutoRepository extends JpaRepository<Produto, Integer> {
   @Transactional(readOnly = true)
   Page<Produto> findDistinctByNomeContainingAndCategoriasIn(String nome,List<Categoria>categorias, Pageable pageRequest);
}