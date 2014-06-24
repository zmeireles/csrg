package com.obc.csrg.util;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.EntityManager;

import org.jboss.seam.log.Log;

import com.obc.csrg.model.Model;

public class QueryUtil<E extends Model> {

	private String order = "";
	
	private String queryString = "";
	private StringBuffer filter = new StringBuffer("");
	private String baseClassname = "";
	private List<Object> parameters = new ArrayList<Object>();
	private String searchTxt = null;
	private ReportFilter reportFilterConstant=null;
	private Log log;
	
	//constructors
	public QueryUtil(String baseClassname,String order){
		this.baseClassname = baseClassname;
		this.order = order;
	}
	//business logic
	
	public void addFilter(ReportFilter filterToAdd){
		if(filterToAdd!=null && !filterToAdd.isEmpty()){
			if(this.filter.length()==0) this.filter.append(filterToAdd.getFilter());
			else this.filter.append(" and ("+filterToAdd.getFilter()+")");
			this.parameters.addAll(filterToAdd.getParameters());
		}
	}
	
	public void cleanFilters(){
		this.parameters.clear();
		this.filter.setLength(0);
	}
	
	public List<E> query(EntityManager entityManager){
		this.createQueryString();
		if(log!=null)log.info("[query] queryString:#0", this.queryString);
		Query query = entityManager.createQuery("select obj "+this.getQueryString() +
				(getOrder().equals("")? "" : " order by " + getOrder()))
				/*.setHint("org.hibernate.cacheable", this.isHibernateCacheable())*/;
		//parameters to apply
		this.applyParameters(query);
		List<E> result = query.getResultList();
		return result;
	}
	
	private void applyParameters(Query q){
		for(int i = 0;i<parameters.size();i++){
			if(log!=null)log.info("[applyParameters] param:#0, value:[#1]", i+1,parameters.get(i));
			q.setParameter(i+1, parameters.get(i));
		}
	}
	
	private void createQueryString(){
		createFilter();
		String query = "";
		query = " from "+ this.getBaseClassname() +" obj ";
		if(this.filter.length()==0)
			this.setQueryString(query);
		else
			this.setQueryString(query + " where " + this.filter.toString());
	}
	
	private void createFilter(){
		//this.parameters.clear(); //limpar os parametros especificos.
		//this.filter.setLength(0);
		//put here fixed or automatic filter for everyone
		if(reportFilterConstant!=null && !reportFilterConstant.isEmpty()) addFilter(reportFilterConstant);
		this.createKeywordsLangFilters();
	}
	
	private void createKeywordsLangFilters(){
		ReportFilter filterToAdd = new ReportFilter();
		String searchTxt = toSearchRegExp(this.getSearchTxt());
		if(searchTxt!=null){
			String[] searchTxtList = this.getSearchTxt().split(" ");
			if(searchTxtList.length>0 && !searchTxtList[0].equals(""))
				filterToAdd.startGroup();
			for(int i=0;i<searchTxtList.length;i++){
				searchTxt = toSearchRegExp(searchTxtList[i]);
				//log.info("[createFilter] searchTxt:#0", searchTxt);
				if(searchTxt!=null && !searchTxtList[i].equals("")){
					filterToAdd.addOrFilter("obj.id in " +
							"(select k.searchValue.objectId from KeywordsLang k where lower(k.keys) like ?)", searchTxt);
				}
			}
			if(searchTxtList.length>0 && !searchTxtList[0].equals(""))
				filterToAdd.endGroup();
		}
		
		//join filters
		this.addFilter(filterToAdd);
	}
	
	/**
	 * returns a string with characters in lower and some literals
	 */
	private String toSearchRegExp(String s){
		if(s==null || s.equals("")) return null;
		String literal = "0123456789bcçdfghjklmnpqrstvwxyz *%+";
		char[] sl = s.trim().toLowerCase().toCharArray();
		StringBuffer result = new StringBuffer("");
		for(char c: sl){
			if(literal.indexOf(c)==-1){
				result.append('_');
			}else{
				result.append(c);
			}
		}
		return "%" + result.toString().replace("*", "%").replace(" ", "%").replace("+", " ") + "%";
	}
	//getters and setters
	
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public String getBaseClassname() {
		return baseClassname;
	}
	public void setBaseClassname(String baseClassname) {
		this.baseClassname = baseClassname;
	}
	
	public String getQueryString() {
		return queryString;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}
	
	public Log getLog() {
		return log;
	}

	public void setLog(Log log) {
		this.log = log;
	}
	
	public String getSearchTxt() {
		return searchTxt;
	}

	public void setSearchTxt(String searchTxt) {
		this.searchTxt = searchTxt;
	}
}
