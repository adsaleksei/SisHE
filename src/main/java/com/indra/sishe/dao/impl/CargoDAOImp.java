package com.indra.sishe.dao.impl;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.indra.infra.dao.exception.RegistroInexistenteException;
import com.indra.sishe.dao.CargoDAO;
import com.indra.sishe.entity.Cargo;

@Repository
public class CargoDAOImp extends NamedParameterJdbcDaoSupport implements CargoDAO{

	@Autowired
	@Resource(mappedName="java:jboss/datasources/SisHE")
	private DataSource dataSource;
	
	private SimpleJdbcInsert insertCargo;
	
	public CargoDAOImp(){
		System.out.println("Criou CargoDaoImpl");
	}
	
	@PostConstruct
	private void init() {
		setDataSource(dataSource);
		insertCargo = new SimpleJdbcInsert(getJdbcTemplate()).withTableName("cargo").usingGeneratedKeyColumns("id");
	}
	
	@Override
	public Cargo save(Cargo entity) {
		Number key = insertCargo.executeAndReturnKey(new BeanPropertySqlParameterSource(entity));
		entity.setId(key.longValue());
		return entity;
	}

	@Override
	public Cargo update(Cargo entity) throws RegistroInexistenteException {
		int rows = getJdbcTemplate().update("UPDATE cargo SET nome = ? "
				+ "WHERE id = ?", entity.getNome(), entity.getId());
		if (rows == 0) throw new RegistroInexistenteException();
		return entity;
	}

	@Override
	public List<Cargo> findAll() {
		return getJdbcTemplate().query("SELECT id AS idCargo, nome AS nomeCargo"
				+ "FROM cargo", new BeanPropertyRowMapper<Cargo>(Cargo.class));
	}

	@Override
	public Cargo findById(Object id) throws RegistroInexistenteException {
		try {
			return getJdbcTemplate().queryForObject("SELECT id, nome "
					+ "FROM cargo WHERE id = ?", new Object[] { id }, new BeanPropertyRowMapper<Cargo>(Cargo.class));
		} catch (EmptyResultDataAccessException e) {
			throw new RegistroInexistenteException();
		}
	}

	@Override
	public EntityManager getEntityManager() {
		return null;
	}

	@Override
	public List<Cargo> findByFilter(Cargo cargoFiltro) {
		
		StringBuilder sql = new StringBuilder();
		MapSqlParameterSource params = new MapSqlParameterSource();
		
		sql.append("SELECT id, nome ");
		sql.append("FROM cargo WHERE 1 = 1");
		
		if (cargoFiltro != null && cargoFiltro.getId() != null) {
			sql.append("AND id = :idCargo");
			params.addValue("idCargo", cargoFiltro.getId());
		}
		if (cargoFiltro != null && cargoFiltro.getNome() != null && !cargoFiltro.getNome().isEmpty()) {
			sql.append("AND LOWER(nome) LIKE '%' || :nomeCargo || '%'");
			params.addValue("nomeCargo", cargoFiltro.getNome().toLowerCase());
		}
		return getNamedParameterJdbcTemplate().query(sql.toString(), params, new BeanPropertyRowMapper<Cargo>(Cargo.class));
	
	}

	@Override
	public Cargo pesquinarNome(String nome) {
		StringBuilder sql = new StringBuilder();
		MapSqlParameterSource params = new MapSqlParameterSource();
		
		sql.append("SELECT id AS idCargo, nome AS nomeCargo ");
		sql.append("FROM cargo WHERE nome like :nomeCargo");
		params.addValue("nomeCargo", nome);
		
		List<Cargo> listaTemp = getNamedParameterJdbcTemplate().query(sql.toString(), params, new BeanPropertyRowMapper<Cargo>(Cargo.class));
		if(!listaTemp.isEmpty()){
			return listaTemp.get(0);
		}else{
			return null;
		}
	}

	@Override
	public void remove(Object id) throws RegistroInexistenteException {
		int rows = getJdbcTemplate().update("DELETE FROM cargo WHERE id = ?", id);
		if (rows == 0) throw new RegistroInexistenteException();
	}


}