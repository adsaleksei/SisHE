package com.indra.sishe.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.indra.infra.dao.exception.DeletarRegistroViolacaoFK;
import com.indra.infra.dao.exception.RegistroDuplicadoException;
import com.indra.infra.dao.exception.RegistroInexistenteException;
import com.indra.sishe.dao.ProjetoDAO;
import com.indra.sishe.entity.Projeto;

@Repository
public class ProjetoJdbcDaoImpl extends NamedParameterJdbcDaoSupport implements ProjetoDAO {

	@Autowired
	private DataSource dataSource;

	private SimpleJdbcInsert insertProjeto;

	@PostConstruct
	private void init() {
		setDataSource(dataSource);
		setInsertProjeto(new SimpleJdbcInsert(getJdbcTemplate()).withTableName("projeto").usingGeneratedKeyColumns("id"));
	}

	@Override
	public Projeto save(Projeto projeto) throws RegistroDuplicadoException {
		MapSqlParameterSource params = new MapSqlParameterSource();
//		params.addValue("id_gerente", projeto.getUsuario().getId());
		params.addValue("nome", projeto.getNome());
		params.addValue("descricao", projeto.getDescricao());

		Number key = insertProjeto.executeAndReturnKey(params);
		projeto.setId(key.longValue());
		return projeto;
	}

	@Override
	public Projeto update(Projeto projeto) throws RegistroInexistenteException {
		int rows = getJdbcTemplate().update("UPDATE projeto SET nome = ?, descricao = ?, id_gerente = ?", projeto.getNome(), projeto.getDescricao(), projeto./*getUsuario().*/getId());
		if (rows == 0)
			throw new RegistroInexistenteException();
		return projeto;
	}

	@Override
	public List<Projeto> findAll() {
		StringBuilder sql = new StringBuilder();
		MapSqlParameterSource params = new MapSqlParameterSource();
		sql.append("SELECT projeto.id AS id, projeto.nome AS nome, usuario.nome AS nomeGerente, usuario.id AS idGerente, projeto.descricao AS descricao ");
		sql.append("FROM projeto INNER JOIN usuario ON (projeto.id_gerente=usuario.id)");
		List<Projeto> lista = getNamedParameterJdbcTemplate().query(sql.toString(), params, new RowMapper<Projeto>() {

			@Override
			public Projeto mapRow(ResultSet rs, int rowNum) throws SQLException {
				Projeto projeto = new Projeto();
//				Usuario usuario = new Usuario();

//				usuario.setId(rs.getLong("idGerente"));
//				usuario.setNome(rs.getString("nomeGerente"));

				projeto.setId(rs.getLong("id"));
//				projeto.setUsuario(usuario);
				projeto.setNome("nome");
				projeto.setDescricao("descricao");
				return projeto;
			}
		});
		return lista;
	}

	@Override
	public Projeto findById(Object id) throws RegistroInexistenteException {return null;}

	@Override
	public void remove(Object id) throws RegistroInexistenteException, DeletarRegistroViolacaoFK { }

	@Override
	public void remove(List<Object> ids) throws RegistroInexistenteException, DeletarRegistroViolacaoFK {
		ArrayList<Object[]> params = new ArrayList<Object[]>(ids.size());
		for (Object id : ids) {
			Object[] param = new Object[] { id };
			params.add(param);
		}
		int[] affectedRows = getJdbcTemplate().batchUpdate("DELETE FROM projeto WHERE id = ?", params);
		for (int rows : affectedRows)
		if (rows == 0) throw new RegistroInexistenteException();
	}

	@Override
	public EntityManager getEntityManager() { return null; }

	@Override
	public List<Projeto> findByFilter(Projeto projeto) {
		StringBuilder sql = new StringBuilder();
		MapSqlParameterSource params = new MapSqlParameterSource();
		sql.append("SELECT projeto.id AS id, projeto.nome AS nome, usuario.nome AS nomeGerente, usuario.id AS idGerente, projeto.descricao AS descricao ");
		sql.append("FROM projeto INNER JOIN usuario ON (projeto.id_gerente=usuario.id) where 1=1 ");

		if (projeto != null && projeto.getNome() != null) {
			sql.append("AND LOWER(projeto.nome) LIKE '%'|| :nomeProjeto || '%' ");
			params.addValue("nomeProjeto", projeto.getNome());
		}
		List<Projeto> lista = getNamedParameterJdbcTemplate().query(sql.toString(), params, 
			new RowMapper<Projeto>() {
			@Override
			public Projeto mapRow(ResultSet rs, int rowNum) throws SQLException {
				Projeto projeto = new Projeto();
//				Usuario usuario = new Usuario();

//				usuario.setId(rs.getLong("idGerente"));
//				usuario.setNome(rs.getString("nomeGerente"));

				projeto.setId(rs.getLong("id"));
//				projeto.setUsuario(usuario);
				projeto.setNome("nome");
				projeto.setDescricao("descricao");
				return projeto;
			}

		});
		return lista;
	}

	public SimpleJdbcInsert getInsertProjeto() {
		return insertProjeto;
	}

	public void setInsertProjeto(SimpleJdbcInsert insertProjeto) {
		this.insertProjeto = insertProjeto;
	}

}