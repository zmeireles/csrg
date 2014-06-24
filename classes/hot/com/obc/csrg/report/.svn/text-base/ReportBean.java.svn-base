package com.obc.csrg.report;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Iterator;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.web.RequestParameter;
import org.jboss.seam.log.Log;
import org.jboss.seam.core.Conversation;

//import com.obc.bqecom.local.FiltroLivreBeanLocal;
import com.obc.csrg.events.ModelEventListener;
import com.obc.csrg.local.StateBeanLocal;
import com.obc.csrg.local.report.ReportBeanLocal;
import com.obc.csrg.model.Model;
import com.obc.csrg.util.ReportFilter;

@Name("reportBean")
@Stateful
@Scope(ScopeType.SESSION)
public class ReportBean<E extends Model> implements ReportBeanLocal<E>,Serializable {

	@Logger 
	protected Log log;
	
	@In
	protected EntityManager entityManager;
	
	@In 
	protected Map<String,String> messages;
	
	@In
	protected StateBeanLocal stateBean;
	
	@In
	private Conversation conversation;
	
	@In
	protected Locale locale;
	
	@RequestParameter
	private Integer newPage;
	
	
	private boolean reportCacheable = true;		//define se usa a cacheHash (caso nao, pode alterar o resultList com override de postQueryProcessing)
	HashMap<Integer,List<E>> cacheHash = new HashMap<Integer,List<E>>();	//resultados em cache
	private final int cacheSize = 300;				//numero de registos total k tira da bd a cada chamada (tem de ser multiplo de pageSize)
	private final int backCacheSizeInPages = 10;	//numero de registos anterior à pagina actual k tira da bd a cada chamada (tem de ser menor k (cacheSize/pageSize))
	protected final int pageSize = 15;				//numero de linhas para cada pagina a aparecer na dataTable
	private int page = 1;							//pagina actual
	protected  final int scrollPageMaxSideSize = 9;	//numero maximo de casas de paginas a colocar antes e depois da pagina actual na scroll da dataTable
	//protected  final int scrollPageMaxSideSizeRe = 9; 
	private String order = "";
	private String queryString = "";
	private StringBuffer filter = new StringBuffer("");
	private String baseClassname = "";
	private List<Object> parameters = new ArrayList<Object>();
	private String searchTxt = null;
	protected Long queryResultCount = null;
	private int queryPageCount = 0;
	private String queryCacheCid = "";						//guarda o conversation id da ultima query k renovou a cache
	private List<E> resultList = null;
	private boolean hibernateCacheable = false;
	private ReportFilter reportFilterConstant;
	
	private Date startDate = null;
	private Date stopDate = null;
	//para definir o uzo ou nao do filtro da data
	private boolean showStartDate = false;
	private boolean showStopDate = false;
	
	//descricao para o searchTxt
	private String descriptionSearchTxt = "";
	
	private boolean queryOutdated = true;
	protected ModelEventListener modelEventListener;
	
	@Begin(join=true)
	@Create
	public void create() {
		//INIT descricao do searchTxt
		descriptionSearchTxt = messages.get("ReportDescSearchTxtGeral");
	}
	@Remove
	public void destroy() {
		log.debug("#0 [remove]", this.toString().substring(this.toString().lastIndexOf(".")));
	}

	
	public String reset(){
		reportCacheable = true;
		cacheHash.clear();
		searchTxt = null;
		descriptionSearchTxt = "";
		order = "";
		queryString = "";
		filter.setLength(0);
		baseClassname = "";
		parameters.clear();
		hibernateCacheable = false;
		queryResultCount = null;
		queryPageCount = 0;
		resultList = null;
		reportFilterConstant = null;
		this.create();
		return null;
	}
	public String reset(String link){
		reset();
		return link;
	}
	
	
	/**
	 * returns a string with characters in lower and some literals
	 */
	protected String toSearchRegExp(String s){
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
	
	//cria a lista de SelectItem de 0->indifrente, 1->sim, 2->nao
	public List<SelectItem> getThreeStatesList() {
		return getObjectComboList(messages.get("OptionIndifferent"),messages.get("OptionYes"),messages.get("OptionNo"));
	}
	//cria uma lista de combo com os objectos em argumentos (list)
	//associados a um int de 0..(list.length-1)
	protected static List<SelectItem> getObjectComboList(String...list){
		List<SelectItem> result = new ArrayList<SelectItem>();
		for(int i=0;i<list.length;i++){
			result.add(new SelectItem(i,list[i]));
		}
		return result;
	}
	
	/*
	 * usado para dar o numero de resultados de resultList
	 */
	public String getResultsMessage(){
		String result;
		this.initQuery();
		if(queryResultCount==null || queryResultCount.equals(0L)) result = messages.get("ResultadoVazio");
		else result = queryResultCount + " " + (queryResultCount.equals(1L) ? messages.get("ResultadoSingular"): messages.get("ResultadoPlural"));
		return result;
	}
	
	protected void createQueryString(){
		createFilter();
		String query = "";
		query = " from "+ this.getBaseClassname() +" obj ";
		if(this.filter.length()==0)
			this.setQueryString(query);
		else
			this.setQueryString(query + " where " + this.filter.toString());
	}
	
	protected void createFilter(){
		this.parameters.clear(); //limpar os parametros especificos.
		this.filter.setLength(0);
		//put here fixed or automatic filter for everyone
		if(reportFilterConstant!=null && !reportFilterConstant.isEmpty()) addFilter(reportFilterConstant);
		this.createKeywordsLangFilters();
	}
	protected void createSimpleFilter(){
		this.parameters.clear(); //limpar os parametros especificos.
		this.filter.setLength(0);
		//put here fixed or automatic filter for everyone
		if(reportFilterConstant!=null && !reportFilterConstant.isEmpty()) addFilter(reportFilterConstant);
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
	
	public String query(){
		//log.trace("[query] start");
		queryOutdated=false;
		
		//if(conversation!=null)
			this.queryCacheCid = conversation.getId();
		createQueryString();
		//log.trace("[query] getQueryString(): #0", getQueryString());
		//log.info("[query] reportCacheable:#0", reportCacheable);
		if(reportCacheable){
			//inicia a pagina
			this.page=1;
			this.cacheHash.clear();
			//faz o count de resultados na max bd
			Query qCount = entityManager.createQuery("select count(obj) "+this.getQueryString())
							/*.setHint("org.hibernate.cacheable", this.isHibernateCacheable())*/;
			this.aplicaParametros(qCount);
			this.queryResultCount = (Long)(qCount.getSingleResult());
			this.queryPageCount = ((queryResultCount.intValue()-1)/pageSize)+1; 
		}else{
			queryAll();
		}
		//log.trace("[query] end");
		return null;
	}
	private void queryHit(){
		//log.trace("[queryHit] start");
		int firstRowToHit = (this.page-this.backCacheSizeInPages)*this.pageSize;
		if(firstRowToHit<0) firstRowToHit=0;
		//log.trace("[queryHit] fstRowToHit: #0", fstRowToHit);
		//prepara a query
		Query query = entityManager.createQuery("select obj "+this.getQueryString() +
				(getOrder().equals("")? "" : " order by " + getOrder()))
				/*.setHint("org.hibernate.cacheable", this.isHibernateCacheable())*/
				.setMaxResults(this.cacheSize)
				.setFirstResult(firstRowToHit);
		//parametros a aplicar
		this.aplicaParametros(query);
		List<E> result = query.getResultList();
		//log.trace("[queryHit] result.size: #0", ((result!=null)?result.size():0));
		if(result!=null || result.size()>0){
			Iterator<E> resultIt = result.iterator();
			List<E> aux = new ArrayList<E>();
			int firstPageToHit = firstRowToHit/this.pageSize;
			//log.trace("[queryHit] fstPageToHit: #0", fstPageToHit);
			while(resultIt.hasNext()){
				aux.add(resultIt.next());
				if(aux.size()==this.pageSize){
					this.cacheHash.put(firstPageToHit, aux);
					firstPageToHit++;
					aux = new ArrayList<E>();
				}
			}
			if(aux.size()>0) this.cacheHash.put(firstPageToHit, aux);
			else firstPageToHit--;
			//log.trace("[queryHit] lastPageToHit: #0", fstPageToHit);
		}
		//log.trace("[queryHit] end");
	}
	private void queryCache(){
		List<E> result = this.cacheHash.get(this.page-1);
		if(result==null && this.queryResultCount!=null && this.queryResultCount.compareTo(0L)>0){
			if(reportCacheable){
				queryHit();
				result = this.cacheHash.get(this.page-1);
			}else{
				//stateBean.log4Debug(this.toString(), "[queryCache] ERROR: Tentativa de actualizar cache com reportCacheable=false, (modo de simulacao)");
			}
		}
		this.setResultList(result);
	}
	private void queryAll(){
		//prepara a query
		Query query = entityManager.createQuery("select obj "+this.getQueryString() +
				(getOrder().equals("")? "" : " order by " + getOrder()))
				/*.setHint("org.hibernate.cacheable", this.isHibernateCacheable())*/;
		//parametros a aplicar
		this.aplicaParametros(query);
		List<E> result = query.getResultList();
		//metodo para alterar os resultados
		result = postQueryProcessing(result);
		//simular para uso de cache
		//inicia a pagina
		this.page=1;
		this.cacheHash.clear();
		queryResultCount = new Long(result.size());
		this.queryPageCount = ((queryResultCount.intValue()-1)/pageSize)+1; 
		if(result!=null || result.size()>0){
			Iterator<E> resultIt = result.iterator();
			List<E> aux = new ArrayList<E>();
			int fstPageToHit = 0;
			//log.trace("[queryAll] fstPageToHit: #0", fstPageToHit);
			while(resultIt.hasNext()){
				aux.add(resultIt.next());
				if(aux.size()==this.pageSize){
					this.cacheHash.put(fstPageToHit, aux);
					fstPageToHit++;
					aux = new ArrayList<E>();
				}
			}
			if(aux.size()>0) this.cacheHash.put(fstPageToHit, aux);
			else fstPageToHit--;
		}
	}
	//metodo para fazer override na subClasse quando nao usa reportCacheable
	protected List<E> postQueryProcessing(List<E> result){
		//pode neste metodo alterar os resultados em argumento
		//e return dos filtrados
		return result;
	}
	protected void aplicaParametros(Query q){
		for(int i = 0;i<parameters.size();i++){
			q.setParameter(i+1, parameters.get(i));
		}
	}
	protected void initQuery(){
		if(!this.queryCacheCid.equals(conversation.getId()) || queryOutdated){
			//stateBean.log4Debug(this.toString(), "[getResultList] query automatico - conversation.id: #0", conversation.getId());
			query();
		}
	}
	public void changePage(){
		//log.info("[changePage] newPage:#0", newPage);
		this.setPage(newPage);
		/*
		//dynamic max scroll size
		int rightPageCount = this.queryPageCount - this.page;
		int leftPageCount = this.page - 1;
		
		if( (rightPageCount < this.scrollPageMaxSideSize) && (leftPageCount <this.scrollPageMaxSideSize))
		{
			if(rightPageCount > leftPageCount)
				this.scrollPageMaxSideSize = rightPageCount-1;
			else
				this.scrollPageMaxSideSize = leftPageCount-1;
		}else if( (rightPageCount > this.scrollPageMaxSideSizeRe) || (leftPageCount > this.scrollPageMaxSideSizeRe))
		{
				this.scrollPageMaxSideSize = scrollPageMaxSideSizeRe;
		}else if( (rightPageCount < this.scrollPageMaxSideSizeRe) && (leftPageCount < this.scrollPageMaxSideSizeRe))
		{
			if(rightPageCount > leftPageCount)
				this.scrollPageMaxSideSize = rightPageCount-1;
			else
				this.scrollPageMaxSideSize = leftPageCount-1;
		}
		//dynamic max scroll size ends
		 */
		
	}
	
	/*
	 * GETTERS AND SETTERS
	 */
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public List<E> getResultList() {
		this.initQuery();
		queryCache();
		//log.info("[getResultList] resultList:#0", resultList);
		return resultList;
	}
	protected void setResultList(List<E> resultList) {
		this.resultList = resultList;
	}
	protected boolean isReportCacheable() {
		return reportCacheable;
	}
	protected void setReportCacheable(boolean reportCacheable) {
		this.reportCacheable = reportCacheable;
	}
	protected boolean isHibernateCacheable() {
		return hibernateCacheable;
	}
	protected void setHibernateCacheable(boolean hibernateCacheable) {
		this.hibernateCacheable = hibernateCacheable;
	}
	protected String getOrder() {
		return order;
	}
	protected void setOrder(String order) {
		this.order = order;
	}
	protected String getQueryString() {
		return queryString;
	}
	protected void setQueryString(String queryString) {
		this.queryString = queryString;
	}
	protected void addFilter(ReportFilter filterToAdd){
		if(filterToAdd!=null && !filterToAdd.isEmpty()){
			if(this.filter.length()==0) this.filter.append(filterToAdd.getFilter());
			else this.filter.append(" and ("+filterToAdd.getFilter()+")");
			this.parameters.addAll(filterToAdd.getParameters());
		}
	}
	protected String getBaseClassname() {
		return baseClassname;
	}
	protected void setBaseClassname(String baseClassname) {
		this.baseClassname = baseClassname;
	}
	protected ReportFilter getReportFilterConstant() {
		return reportFilterConstant;
	}
	protected void setReportFilterConstant(ReportFilter reportFilterConstant) {
		this.reportFilterConstant = reportFilterConstant;
	}
	
	//PARA O SCROLL
	public boolean isShowFirstPage(){
		return (this.page==1);
	}
	public boolean isShowLastPage(){
		return (this.page==this.queryPageCount);
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
		return this.queryPageCount;
	}
	public List<Integer> getScrollPageToRender(){
		List<Integer> result = new ArrayList<Integer>();
		int fstPageToShow = this.page-this.scrollPageMaxSideSize;
		if(fstPageToShow<1) fstPageToShow = 1;
		int lastPageToShow = this.page+this.scrollPageMaxSideSize;
		if(lastPageToShow>this.queryPageCount) lastPageToShow = this.queryPageCount;

		for(int i=fstPageToShow; i<=lastPageToShow; i++){
			result.add(i);
		}
		return result;
	}
	public boolean isShowScrollDataTable(){
		return (this.queryResultCount!=null && this.queryPageCount>1);
	}
	
	//PARAMETROS GENERICOS
	public String getSearchTxt() {
		return searchTxt;
	}
	public void setSearchTxt(String searchTxt) {
		this.searchTxt = searchTxt;
	}
	public String getDescriptionSearchTxt() {
		return descriptionSearchTxt;
	}
	public void setDescriptionSearchTxt(String descriptionSearchTxt) {
		this.descriptionSearchTxt = descriptionSearchTxt;
	}
	
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getStopDate() {
		return stopDate;
	}
	public void setStopDate(Date stopDate) {
		this.stopDate = stopDate;
	}
	public boolean isShowStartDate() {
		return showStartDate;
	}
	public void setShowStartDate(boolean showStartDate) {
		this.showStartDate = showStartDate;
	}
	public boolean isShowStopDate() {
		return showStopDate;
	}
	public void setShowStopDate(boolean showStopDate) {
		this.showStopDate = showStopDate;
	}
	
	public boolean isQueryOutdated() {
		return queryOutdated;
	}
	public void setQueryOutdated(boolean queryOutdated) {
		this.queryOutdated = queryOutdated;
	}
	
	public boolean isPageRendered(int currentPage)
	{
		return (this.page != currentPage);

	}
	
}
