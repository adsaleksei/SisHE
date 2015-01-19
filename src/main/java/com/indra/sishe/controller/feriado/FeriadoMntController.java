package com.indra.sishe.controller.feriado;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import com.indra.infra.resource.MessageProvider;
import com.indra.infra.service.exception.ApplicationException;
import com.indra.sishe.entity.Estado;
import com.indra.sishe.entity.Feriado;

@ViewScoped
@ManagedBean(name = "feriadoMnt")
public class FeriadoMntController extends FeriadoController{
	
	private static final long serialVersionUID = -3390362083151653812L;
	
	private List<Feriado> listaFeriados;
	private List<Feriado> feriadosSelecionados;

	public FeriadoMntController() {
	}
	
	@PostConstruct
	private void init(){
		MessageProvider.setInstance(messageProvider);
		setListaEstado(obterEstados());
		getListaEstado().add(0, new Estado(new Long(0), "Nacional", "BR"));
		
		searched = (Boolean) getFlashAttr("searched");
		if (searched == null) searched = false;
		
		feriadoFiltro = (Feriado) getFlashAttr("feriadoFiltro");
		if (feriadoFiltro == null) feriadoFiltro = new Feriado();
		
		if (!searched) listaFeriados = new ArrayList<Feriado>();
		else pesquisar();
	}

	public void pesquisar() {
		listaFeriados = feriadoService.findByFilter(feriadoFiltro);
		Collections.sort(listaFeriados);
		searched = true;
	}
	
public List<Feriado> getFeriadosSelecionados() {
		return feriadosSelecionados;
	}

	public void setFeriadosSelecionados(List<Feriado> feriadosSelecionados) {
		this.feriadosSelecionados = feriadosSelecionados;
	}

public void beforeRemoveFeriado() {
		
		if (feriadosSelecionados.size() == 0) {
			RequestContext.getCurrentInstance().execute("selectAtleastOne.show()");
		} else {
			RequestContext.getCurrentInstance().execute("confirmExclusao.show()");
		}
	}
	
	public String remove(){
		
		int size = feriadosSelecionados.size();
		ArrayList<Long> ids = new ArrayList<Long>(size);
		for (Feriado feriado : feriadosSelecionados){
			ids.add(feriado.getId());
		}
		try {
			feriadoService.remove(ids);
			messager.info(messageProvider.getMessage("msg.success.registro.excluido", "Feriado"));
		} catch (Exception e) {
			messager.error(e.getMessage());
		}
		pesquisar();
		return irParaConsultar();
	}
	
	public String irParaAlterar(Feriado feriado){
		putFlashAttr("searched", searched);
		putFlashAttr("feriadoFiltro", feriadoFiltro);
		try {
			feriado = feriadoService.findById(feriado.getId());
			putFlashAttr("feriadoSelecionado", feriado);
			return irParaAlterar();
		} catch (ApplicationException e) {
			returnErrorMessage(e.getMessage());
			return irParaConsultar();
		}
	}
	
	public List<Feriado> getListaFeriados() {
		return listaFeriados;
	}

	public void setListaFeriados(List<Feriado> listaFeriados) {
		this.listaFeriados = listaFeriados;
	}
	
	public Feriado getFeriadoFiltro() {
		return feriadoFiltro;
	}

	public void setFeriadoFiltro(Feriado feriadoFiltro) {
		this.feriadoFiltro = feriadoFiltro;
	}
	
}