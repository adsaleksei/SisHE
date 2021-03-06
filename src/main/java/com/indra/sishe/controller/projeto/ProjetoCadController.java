package com.indra.sishe.controller.projeto;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.indra.infra.resource.MessageProvider;
import com.indra.infra.service.exception.ApplicationException;
import com.indra.sishe.entity.Cargo;
import com.indra.sishe.entity.Projeto;
import com.indra.sishe.entity.Usuario;

@ViewScoped
@ManagedBean(name = "projetoCad")
public class ProjetoCadController extends ProjetoController {

	private static final long serialVersionUID = 5528166823228155750L;

	public Projeto projetoSelecionado;

	public List<Usuario> listaGerentes;

	public ProjetoCadController() {
	}

	@PostConstruct
	private void init() {
		
		MessageProvider.setInstance(messageProvider);
		setListaGerentes(ObterGerentes());
		
		searched = (Boolean) getFlashAttr("searched");
		projetoSelecionado = (Projeto) getFlashAttr("projetoSelecionado");
		if (projetoSelecionado == null) {
			projetoSelecionado = new Projeto();
		}
		
		projetoFiltro = (Projeto) getFlashAttr("projetoFiltro");
	}

	public String cadastrarProjeto() {
		try {
			projetoService.save(projetoSelecionado);
			putFlashAttr("projetoFiltro", projetoFiltro);
			returnInfoMessage(messageProvider.getMessage("msg.success.registro.cadastrado", "Projeto"));
			putFlashAttr("searched", searched);
			return irParaConsultar();
		} catch (Exception e) {
			returnErrorMessage(e.getMessage());
		}
		return null;
	}

	public String confirmar() {
		if (modoCadastrar()) {
			return cadastrarProjeto();
		} else {
			return alterarProjeto();
		}
	}

	public String alterarProjeto() {
		try {
			projetoService.update(projetoSelecionado);
			returnInfoMessage(messageProvider.getMessage("msg.success.registro.alterado", "Projeto"));
			putFlashAttr("projetoFiltro", projetoFiltro);
			putFlashAttr("searched", searched);
			return irParaConsultar();
		} catch (ApplicationException e) {
			returnErrorMessage(e.getMessage());
			return irParaAlterar(projetoSelecionado);
		}
	}

	public List<Usuario> ObterGerentes() {
		Cargo cargo = new Cargo();
		cargo.setId(new Long(2));
		return usuarioService.findByCargo(cargo);
	}

	public String cancelar() {
		putFlashAttr("searched", searched);
		putFlashAttr("projetoFiltro", projetoFiltro);
		return irParaConsultar();
	}

	public boolean modoCadastrar() {
		if (projetoSelecionado == null || projetoSelecionado.getId() == null) {
			return true;
		} else {
			return false;
		}
	}

	public Projeto getProjetoSelecionado() {
		return projetoSelecionado;
	}

	public void setProjetoSelecionado(Projeto projetoSelecionado) {
		this.projetoSelecionado = projetoSelecionado;
	}

	public boolean wasSearched() {
		return searched;
	}

	public void setPesquisar(boolean pesquisar) {
		this.searched = pesquisar;
	}

	public List<Usuario> getListaGerentes() {
		return listaGerentes;
	}

	public void setListaGerentes(List<Usuario> listaGerentes) {
		this.listaGerentes = listaGerentes;
	}

}
