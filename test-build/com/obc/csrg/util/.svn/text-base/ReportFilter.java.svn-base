package com.obc.csrg.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ReportFilter implements Serializable {

	private StringBuffer filter = new StringBuffer("");
	private List<Object> parameters = new ArrayList<Object>();
	
	
	public ReportFilter(){
		super();
	}
	public ReportFilter(String filter, Object...params){
		super();
		this.addFilter(filter, params);
	}
	
	public void addFilter(String filter, Object...params){
		if(this.filter.length()==0) 
			this.filter.append(filter);
		else{
			this.colocaFiltro(" and ", filter);
		}
		for(int i=0; i<params.length; i++){
			this.parameters.add(params[i]);
		}
	}
	public void startGroup(){
		this.filter.append(" (");
	}
	public void endGroup(){
		this.filter.append(") ");
	}
	public void addOrFilter(String filter, Object...params){
		if(this.filter.length()==0) 
			this.filter.append(filter);
		else
			this.colocaFiltro(" or ", filter);
		for(int i=0; i<params.length; i++){
			this.parameters.add(params[i]);
		}
	}
	private void colocaFiltro(String operador,String filtro){
		int numChars = this.filter.length();
		char chr = this.filter.charAt(numChars-1);
		if(chr=='(')
			this.filter.append(filtro);
		else
			this.filter.append(operador + filtro);
	}
	public String getFilter() {
		return filter.toString();
	}
	public List<Object> getParameters() {
		return parameters;
	}
	
	public void clear(){
		filter.setLength(0);
		parameters.clear();
	}
	
	public boolean isEmpty(){
		return filter.length()==0;
	}
}
