package com.indra.sishe.controller.cliente;

import java.io.Serializable;

import javax.inject.Inject;

import com.indra.infra.controller.BaseController;
import com.indra.sishe.entity.Cliente;
import com.indra.sishe.service.ClienteService;

public class ClienteController extends BaseController implements Serializable {
	
	private static final long serialVersionUID = 4494058871735106555L;
	
	@Inject
	protected transient ClienteService clienteService;

	public Cliente clienteFiltro;
	
	public Cliente clienteSelecionado;
	
	protected Boolean searched;

	
	public boolean validarCliente(Cliente clienteSelecionado) {

		if (clienteSelecionado.getNomeCliente().isEmpty()) {
			// nome não foi preenchido.
			messager.error(messageProvider.getMessage("msg.error.campo.obrigatorio", "Nome"));

		} else if (clienteSelecionado.getNomeCliente().length() > 40) {
			// o nome ultrapassa 40 caracteres.
			messager.error(messageProvider.getMessage("msg.error.campo.maior.esperado", "Nome", "40"));
		} else {
			return true;
		}
		return false;
	}
	
	public String irParaConsultar() {
		return "/paginas/cliente/consultarCliente.xhtml?faces-redirect=true";
	}

	public String irParaCadastrar() {
		putFlashAttr("searched", searched);
		putFlashAttr("clienteFiltro", clienteFiltro);
		return "/paginas/cliente/cadastrarCliente.xhtml?faces-redirect=true";
	}

	public String irParaAlterar(Cliente clienteSelecionado) {
		putFlashAttr("searched", searched);
		putFlashAttr("clienteFiltro", clienteFiltro);
		putFlashAttr("clienteSelecionado", clienteSelecionado);
		return "/paginas/cliente/alterarCliente.xhtml?faces-redirect=true";
	}
	
}
