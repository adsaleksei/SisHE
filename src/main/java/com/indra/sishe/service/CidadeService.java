package com.indra.sishe.service;

import java.util.List;

import javax.ejb.Local;

import com.indra.infra.service.BaseService;
import com.indra.sishe.entity.Cidade;
import com.indra.sishe.enums.EstadoEnum;

@Local
public interface CidadeService extends BaseService<Cidade>{
	
	public List<Cidade> findByEstado(EstadoEnum estado);

}
