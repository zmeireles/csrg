package com.obc.csrg.report;

import javax.ejb.Stateful;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import com.obc.csrg.local.report.DepartmentReportBeanLocal;
import com.obc.csrg.model.Department;
import com.obc.csrg.util.ReportFilter;

@Name("departmentReport")
@Stateful
@Scope(ScopeType.SESSION)
public class DepartmentReportBean extends ReportBean<Department> implements DepartmentReportBeanLocal{
	
	@Override
	public void create(){
		super.create();
		this.setDescriptionSearchTxt(messages.get("ReportDescSearchTxtTipoOferta"));
	}
	
	@Override
	public String query(){
		
		this.setBaseClassname("Department");
		this.setOrder("obj.name");
		this.setHibernateCacheable(true);
		String result = super.query();
		log.info("[query] result:#0", super.getResultList());
		return result;
	}
	
	@Override
	public void createFilter(){
		//Initial filter creation including free filters if defined
		super.createFilter();

		// this serves as an example of specific filters
		/*
		ReportFilter filterToAdd = new ReportFilter();
		//specific filters
		String searchTxt = toSearchRegExp(this.getSearchTxt());
		if(searchTxt!=null){
			filterToAdd.addFilter("(lower(obj.name) like ?)", searchTxt);
		}
		
		//join of filters
		this.addFilter(filterToAdd);
		*/
	}
	
}
