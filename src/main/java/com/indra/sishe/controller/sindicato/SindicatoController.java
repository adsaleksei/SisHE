package com.indra.sishe.controller.sindicato;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import com.indra.infra.controller.BaseController;
import com.indra.sishe.entity.Estado;
import com.indra.sishe.entity.Sindicato;
import com.indra.sishe.service.EstadoService;
import com.indra.sishe.service.SindicatoService;

public class SindicatoController extends BaseController implements Serializable {

	private static final long serialVersionUID = 1L;

	// VARI�VEL UTILIZADA PARA A UTILIZA��O DOS SERVI�OS
	@Inject
	protected transient SindicatoService sindicatoService;

	@Inject
	protected transient EstadoService estadoService;
	protected List<Estado> listaEstado;

	// VARI�VEL UTILIZADA PARA O FILTRO DA PESQUISA
	public Sindicato sindicatoFiltro;

	// VARI�VEL UTILIZADA PARA EXCLUIR OU ALTERAR
	public Sindicato sindicatoSelecionado;

	// TRUE QUANDO O BOT�O PESQUISAR FOR PRESSIONADO
	protected Boolean searched;

	/**
	 * @return the listaEstado
	 */
	public List<Estado> getListaEstado() {
		return listaEstado;
	}

	/**
	 * @param listaEstado
	 *            the listaEstado to set
	 */
	public void setListaEstado(List<Estado> listaEstado) {
		this.listaEstado = listaEstado;
	}

	// VALIDA O SINDICATO PELO TAMANHO DO NOME E SE EST� VAZIO
	public boolean validarSindicato(Sindicato sindicatoFiltro) {
		if (!verificarDescricao(sindicatoFiltro)
				|| !tamanhoDescricao(sindicatoFiltro)) {
			return false;
		} else {
			return true;
		}
	}

	// VALIDA O NOME DO SINDICATO PARA A PESQUISA; CASO SEJA VAZIO OU NULL
	// RETORNAR� FALSE
	public boolean verificarDescricao(Sindicato sindicatoFiltro) {
		if (sindicatoFiltro.getDescricao().isEmpty()
				|| sindicatoFiltro.getDescricao() == "") {
			messager.error(messageProvider.getMessage(
					"msg.error.campo.obrigatorio", "Nome do Sindicato"));
			return false;
		} else {
			return true;
		}
	}

	// VERIFICA O TAMANHO DA DESCRIC�O DO SINDICATO; SE FOR MAIOR QUE 50
	// RETORNAR� FALSE
	// TAMANHO DE ACORDO COM O TAMANHO DEFINIDO NO BD E COMPONENTE INPUT
	public boolean tamanhoDescricao(Sindicato sindicatoSelecionado) {
		if (sindicatoSelecionado.getDescricao().length() > 40) {
			messager.error(messageProvider
					.getMessage("msg.error.campo.maior.esperado",
							"Nome do Sindicato", "40"));
			return false;
		} else {
			return true;
		}
	}

	public String irParaConsultar() {
		return "/paginas/sindicato/consultarSindicato.xhtml?faces-redirect=true";
	}

	public String irParaCadastrar() {
		putFlashAttr("searched", searched);
		putFlashAttr("sindicatoFiltro", sindicatoFiltro);
		return "/paginas/sindicato/cadastrarSindicato.xhtml?faces-redirect=true";
	}

	public String irParaAlterar(Sindicato sindicatoSelecionado) {
		putFlashAttr("searched", searched);
		putFlashAttr("sindicatoFiltro", sindicatoFiltro);
		putFlashAttr("sindicatoSelecionado", sindicatoSelecionado);
		return "/paginas/sindicato/alterarSindicato.xhtml";
	}

}
