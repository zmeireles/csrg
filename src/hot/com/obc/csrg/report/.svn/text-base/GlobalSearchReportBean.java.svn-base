package com.obc.csrg.report;

import java.util.List;
import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.ejb.Stateful;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import com.obc.csrg.util.PagedArrayList;
import com.obc.csrg.local.report.GlobalSearchReportBeanLocal;
import com.obc.csrg.model.KeywordsLang;
import com.obc.csrg.util.ReportFilter;
import com.obc.csrg.search.SearchResultModel;
import com.obc.csrg.model.SearchObject;
import com.obc.csrg.model.SearchProperty;
import com.obc.csrg.model.SearchValue;

@Name("globalSearchReport")
@Stateful
@Scope(ScopeType.SESSION)
public class GlobalSearchReportBean extends ReportBean<KeywordsLang> implements GlobalSearchReportBeanLocal{
	
	private PagedArrayList<SearchResultModel> searchResults;
	
	private int resultsCount=0;
	
	@Override
	public void create(){
		super.create();
		this.setReportCacheable(false);
		this.searchResults = new PagedArrayList<SearchResultModel>(messages,this.pageSize,this.scrollPageMaxSideSize);
		//this.setDescriptionSearchTxt(messages.get("ReportDescSearchTxtTipoOferta"));
	}
	
	@Override
	public String query(){
		
		this.setBaseClassname("KeywordsLang");
		this.setOrder("");
		//this.setHibernateCacheable(true);
		String result = super.query();
		log.info("[query] result:#0", super.getResultList());
		result = "globalSearchResult";
		return result;
	}
	
	@Override
	public void createFilter(){
		//creation of initial filters including free filters if defined
		//super.createFilter(); //don't use the super.createFilter()
		
		super.createSimpleFilter();
		
		log.info("[createFilter] searchTxt:#0", this.getSearchTxt());
		
		ReportFilter filterToAdd = new ReportFilter();
		String searchTxt = toSearchRegExp(this.getSearchTxt().trim());
		if(searchTxt!=null && !searchTxt.equals("")){
			String[] searchTxtList = this.getSearchTxt().split(" ");
			if(searchTxtList.length>0 && !searchTxtList[0].equals(""))
				filterToAdd.startGroup();
			for(int i=0;i<searchTxtList.length;i++){
				searchTxt = toSearchRegExp(searchTxtList[i]);
				//log.info("[createFilter] searchTxt:#0", searchTxt);
				if(searchTxt!=null && !searchTxtList[i].equals("")){
					filterToAdd.addOrFilter("lower(obj.keys) like ?)", searchTxt);
				}
			}
			if(searchTxtList.length>0 && !searchTxtList[0].equals(""))
				filterToAdd.endGroup();
		}
		
		//join filters
		this.addFilter(filterToAdd);
	}
	@Override
	protected List<KeywordsLang> postQueryProcessing(List<KeywordsLang> results){
		log.info("[postQueryProcessing] results:#0", results.size());
		this.scoreAndSortResults(results);
		results.clear();
		return results;
	}
	
	private void scoreAndSortResults(List<KeywordsLang> results){
		this.searchResults.clear();
		if(results==null || results.size()==0)
			return;
		List<SearchValue> values = new ArrayList<SearchValue>();
		List<SearchProperty> properties = new ArrayList<SearchProperty>();
		List<SearchObject> objects = new ArrayList<SearchObject>();
		
		//identify distinct objects, properties and models
		for(KeywordsLang key:results){
			if(key.getSearchValue()!=null && !key.getSearchValue().isObjectInList(values))
				values.add(key.getSearchValue());
			else if(key.getSearchProperty()!=null && !key.getSearchProperty().isObjectInList(properties))
				properties.add(key.getSearchProperty());
			else if(key.getSearchObject()!=null && !key.getSearchObject().isObjectInList(objects))
				objects.add(key.getSearchObject());
		}
		//for each type score and add to results (for now just use values)
		SortedSet<SearchResultModel> orderedSet = new TreeSet<SearchResultModel>();
		for(SearchValue value:values){
			SearchResultModel srm = new SearchResultModel(value,entityManager);
			srm.setLog(log);
			srm.computeMatchPercent(this.getSearchTxt());
			orderedSet.add(srm);
		}
		List<SearchResultModel> orderedList = new ArrayList<SearchResultModel>();
		for(SearchResultModel srm:orderedSet){
			if(srm!=null && srm.getInstance()!=null){
				//log.info("[scoreAndSortResults], params)
				orderedList.add(srm);
			}
		}
		this.searchResults.clear();
		this.searchResults.addAll(orderedList);
	}
	
	
	//getters and setters
	public PagedArrayList<SearchResultModel> getSearchResults() {
		this.initQuery();
		return searchResults;
	}

	public void setSearchResults(PagedArrayList<SearchResultModel> searchResults) {
		this.searchResults.clear();
		this.searchResults.addAll(searchResults);
		//this.searchResults = searchResults;
	}
	
	public int getResultsCount() {
		return this.searchResults.size();
	}

	public void setResultsCount(int resultsCount) {
		this.resultsCount = resultsCount;
	}

	//override some methods to be able to list items
	@Override
	public String getResultsMessage(){
		this.initQuery();
		return this.searchResults.getResultsMessage();
	}
	@Override
	public int getPage() {
		return this.searchResults.getPage();
	}
	@Override
	public void setPage(int page) {
		this.searchResults.setPage(page);
	}
	@Override
	public boolean isShowFirstPage(){
		return this.searchResults.isShowFirstPage();
	}
	@Override
	public boolean isShowLastPage(){
		return this.searchResults.isShowLastPage();
	}
	@Override
	public int getFirstPage(){
		return this.searchResults.getFirstPage();
	}
	@Override
	public int getPreviousPage(){
		return this.searchResults.getPreviousPage();
	}
	@Override
	public int getNextPage(){
		return this.searchResults.getNextPage();
	}
	@Override
	public int getLastPage(){
		return this.searchResults.getLastPage();
	}
	@Override
	public List<Integer> getScrollPageToRender(){
		return this.searchResults.getScrollPageToRender();
	}
	@Override
	public boolean isShowScrollDataTable(){
		return this.searchResults.isShowScrollDataTable();
	}
}
