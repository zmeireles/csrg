package com.obc.csrg.report;

import javax.ejb.Stateful;
import javax.persistence.Query;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import com.obc.csrg.local.report.WebpageReportBeanLocal;
import com.obc.csrg.model.Webpage;
import com.obc.csrg.util.ReportFilter;

@Name("webpageReport")
@Stateful
@Scope(ScopeType.SESSION)
public class WebpageReportBean extends ReportBean<Webpage> implements WebpageReportBeanLocal{
	
	@Override
	public void create(){
		super.create();
		this.setDescriptionSearchTxt(messages.get("ReportDescSearchTxtTipoOferta"));
	}
	
	@Override
	public String query(){
		
		this.setBaseClassname("Webpage");
		this.setOrder("obj.html");
		this.setHibernateCacheable(true);
		String result = super.query();
		log.info("[query] result:#0", super.getResultList());
		return result;
	}
	
	@Override
	public void createFilter(){
		//Criação do filtro inicial com os filtros livres se estiverem definidos
		super.createFilter();

	}
	
}
