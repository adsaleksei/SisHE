package com.indra.sishe.controller.sistema;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import com.indra.infra.controller.BaseController;
import com.indra.sishe.entity.Sistema;
import com.indra.sishe.service.SistemaService;

public class SistemaController extends BaseController implements Serializable {

	private static final long serialVersionUID = -2097043422349464567L;
	@Inject
	protected transient SistemaService sistemaService;
	protected List<Sistema> listaSistema;

	// VARI�VEL UTILIZADA PARA O FILTRO DA PESQUISA
	public Sistema sistemaFiltro;

	// VARI�VEL UTILIZADA PARA EXCLUIR OU ALTERAR
	public Sistema sistemaSelecionado;

	// TRUE QUANDO O BOT�O PESQUISAR FOR PRESSIONADO
	protected Boolean searched;

	public SistemaController() {
		// TODO Auto-generated constructor stub
	}

	// FALTA ADICIONAR A VERIFICA��O PARA PROJETO
	public boolean validarSistema(Sistema sistemaSelecionado) {

		if (sistemaSelecionado != null) {

			if (sistemaSelecionado.getDescricao().length() > 200) {
				messager.error(messageProvider.getMessage(
						"msg.error.campo.maior.esperado", "Descri��o"));
				return false;
			} else if (sistemaSelecionado.getDescricao() == null) {
				messager.error(messageProvider.getMessage(
						"msg.error.campo.obrigatorio", "Descri��o"));
				return false;
			} else if (sistemaSelecionado.getNome().length() > 50) {
				messager.error(messageProvider.getMessage(
						"msg.error.campo.maior.esperado", "Nome"));
				return false;
			} else if (sistemaSelecionado.getNome() == null) {
				messager.error(messageProvider.getMessage(
						"msg.error.campo.obrigatorio", "Nome"));
				return false;
			} else if (sistemaSelecionado.getUsuario() == null) {
				messager.error(messageProvider.getMessage(
						"msg.error.campo.maior.esperado", "Lider"));
				return false;
			} else {
				return true;
			}

		} else {
			return true;
		}
	}

	public String irParaConsultar() {
		return "/paginas/sistema/consultarSistema.xhtml?faces-redirect=true";
	}

	public String irParaCadastrar() {
		putFlashAttr("searched", searched);
		putFlashAttr("sistemaFiltro", sistemaFiltro);
		return "/paginas/sistema/cadastrarSistema.xhtml?faces-redirect=true";
	}

	public String irParaAlterar(Sistema sistemaSelecionado) {
		putFlashAttr("searched", searched);
		putFlashAttr("sistemaFiltro", sistemaFiltro);
		putFlashAttr("sistemaSelecionado", sistemaSelecionado);
		return "/paginas/sistema/cadastrarrSistema.xhtml";
	}

}
