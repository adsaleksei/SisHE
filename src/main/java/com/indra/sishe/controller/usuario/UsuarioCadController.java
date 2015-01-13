package com.indra.sishe.controller.usuario;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import com.indra.infra.resource.MessageProvider;
import com.indra.infra.service.exception.ApplicationException;
import com.indra.sishe.entity.Cargo;
import com.indra.sishe.entity.Usuario;
import com.indra.sishe.service.CargoService;

@ViewScoped
@ManagedBean(name = "usuarioCad")
public class UsuarioCadController extends UsuarioController {

	private static final long serialVersionUID = -7129370270911504298L;

	@Inject
	protected transient CargoService cargoService;

	protected Usuario usuarioSelecionado;

	/*
	 * @Inject protected transient SindicatoService sindicatoService;
	 */

	public UsuarioCadController() {
	}

	@PostConstruct
	private void init() {

		MessageProvider.setInstance(messageProvider);

		searched = (Boolean) getFlashAttr("searched");

		usuarioSelecionado = (Usuario) getFlashAttr("usuarioSelecionado");
		if (usuarioSelecionado == null) usuarioSelecionado = new Usuario();

		usuarioFiltro = (Usuario) getFlashAttr("usuarioFiltro");
	}

	public String cadastrarUsuario() {
		if (validarUsuario(usuarioSelecionado)) {
			try {
				usuarioService.save(usuarioSelecionado);
				putFlashAttr("usuarioFiltro", usuarioFiltro);
				returnInfoMessage(messageProvider.getMessage("msg.success.registro.cadastrado", "Usuario"));
				putFlashAttr("searched", searched);
				return irParaConsultar();
			} catch (ApplicationException e) {
				returnErrorMessage(e.getMessage());
			}
		}
		return null;
	}

	public String alterarUsuario() {
		if (validarUsuario(usuarioSelecionado)) {
			try {
				usuarioService.update(usuarioSelecionado);
				returnInfoMessage(messageProvider.getMessage("msg.success.registro.alterado", "Usuario"));
				putFlashAttr("usuarioFiltro", usuarioFiltro);
				putFlashAttr("searched", searched);
				return irParaConsultar();
			} catch (ApplicationException e) {
				returnErrorMessage(e.getMessage());
				return irParaAlterar(usuarioSelecionado);
			}
		}
		return null;
	}

	public String cancelar() {
		putFlashAttr("searched", searched);
		putFlashAttr("usuarioFiltro", usuarioFiltro);
		return irParaConsultar();
	}

	private boolean modoCadastrar() {
		if (usuarioSelecionado == null || usuarioSelecionado.getId() == null) {
			return true;
		} else {
			return false;
		}
	}

	public String confirmar() {
		if (modoCadastrar()) {
			return cadastrarUsuario();
		} else {
			return alterarUsuario();
		}
	}

	public List<Cargo> obterCargos() {
		return cargoService.findAll();
	}

	public Usuario getUsuarioSelecionado() {
		return usuarioSelecionado;
	}

	public void setUsuarioSelecionado(Usuario usuarioSelecionado) {
		this.usuarioSelecionado = usuarioSelecionado;
	}

	/*
	 * public List<Sindicato> obterSindicatos(){ return
	 * sindicatoService.findAll(); }
	 */
}