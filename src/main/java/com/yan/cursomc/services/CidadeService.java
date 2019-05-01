package com.yan.cursomc.services;

import java.util.List;

import com.yan.cursomc.domain.Cidade;
import com.yan.cursomc.repositories.CidadeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CidadeService {
    @Autowired
	private CidadeRepository repo;

	public List<Cidade> findByEstado(Integer estadoId) {
		return repo.findCidades(estadoId);
	}
    
}