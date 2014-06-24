package com.obc.csrg.report;

import javax.ejb.Stateful;
import javax.persistence.Query;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import com.obc.csrg.local.report.SessionLogReportBeanLocal;
import com.obc.csrg.model.SessionLog;
import com.obc.csrg.util.ReportFilter;

@Name("sessionLogReport")
@Stateful
@Scope(ScopeType.SESSION)
public class SessionLogReportBean extends ReportBean<SessionLog> implements SessionLogReportBeanLocal{
	
	@Override
	public void create(){
		super.create();
		this.setDescriptionSearchTxt(messages.get("ReportDescSearchTxtTipoOferta"));
	}
	
	@Override
	public String query(){
		
		this.setBaseClassname("SessionLog");
		this.setOrder("obj.beginDate desc");
		this.setHibernateCacheable(true);
		String result = super.query();
		//log.info("[query] result:#0", super.getResultList());
		return result;
	}
	
	@Override
	public void createFilter(){
		//creation of initial filters including free filters if defined
		super.createFilter();
		//for specific filters see UserReportBean commented code
	}
	
}
