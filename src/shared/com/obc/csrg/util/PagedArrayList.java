package com.obc.csrg.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.RandomAccess;

public class PagedArrayList<E> extends ArrayList<E> implements List<E>, RandomAccess, Cloneable, java.io.Serializable {

	private int pageSize = 15;				//number of lines for each page in data table
	private int page = 1;					//current page
	private int scrollPageMaxSideSize = 9;	//max number of direct pages access before and after current page
	private List<E> resultList = new ArrayList<E>();
	private String noResults = "Nenhum Resultado";
	private String singleResult = "Resultado";
	private String multipleResults = "Resultados";


	public PagedArrayList(Map<String,String> messages) {
		super();
		this.setMessages(messages);
	}
	public PagedArrayList(Map<String,String> messages, int pageSize) {
		super();
		this.setMessages(messages);
		this.pageSize = pageSize;
	}
	public PagedArrayList(Map<String,String> messages, int pageSize, int scrollPageMaxSideSize) {
		super();
		this.setMessages(messages);
		this.pageSize = pageSize;
		this.scrollPageMaxSideSize = scrollPageMaxSideSize;
	}
	
	private void setMessages(Map<String,String> messages){
		if(messages!=null){
			noResults = messages.get("PALNoResults");
			singleResult = messages.get("PALSingleResult");
			multipleResults = messages.get("PALMultipleResults");
		}
	}
	
	@Override
	public void clear(){
		super.clear();
		this.page=1;
	}
	
	/*
	 * used to get list result count
	 */
	public String getResultsMessage(){
		String result;
		if(this.size()==0) result = noResults;
		else result = this.size() + " " + ((this.size()==1) ? singleResult: multipleResults);
		return result;
	}
	
	//manage visualization page
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		if(page<1) page=1;
		if(page>getLastPage()) page=getLastPage();
		this.page = page;
	}
	public List<E> getResultList(){
		resultList.clear();
		int fromIndex = (page-1)*pageSize;
		int toIndex = (page)*pageSize;
		if(toIndex>this.size()) toIndex = this.size();
		resultList.addAll(this.subList(fromIndex, toIndex));
		return resultList;
	}
	
	//PARA O SCROLL
	public boolean isShowFirstPage(){
		return (this.page==1);
	}
	public boolean isShowLastPage(){
		return (this.page==getLastPage());
	}
	public int getFirstPage(){
		return 1;
	}
	public int getPreviousPage(){
		return this.page-1;
	}
	public int getNextPage(){
		return this.page+1;
	}
	public int getLastPage(){
		return ((this.size()-1)/pageSize)+1;
	}
	public List<Integer> getScrollPageToRender(){
		List<Integer> result = new ArrayList<Integer>();
		int fstPageToShow = this.page-this.scrollPageMaxSideSize;
		if(fstPageToShow<1) fstPageToShow = 1;
		int lastPageToShow = this.page+this.scrollPageMaxSideSize;
		if(lastPageToShow>getLastPage()) lastPageToShow = getLastPage();
		for(int i=fstPageToShow; i<=lastPageToShow; i++){
			result.add(i);
		}
		return result;
	}
	public boolean isShowScrollDataTable(){
		return (getLastPage()>1);
	}
	
}
