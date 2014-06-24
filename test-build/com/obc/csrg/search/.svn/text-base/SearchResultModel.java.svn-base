package com.obc.csrg.search;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;

import org.jboss.seam.log.Log;

import com.obc.csrg.model.Model;
import com.obc.csrg.model.KeywordsLang;
import com.obc.csrg.model.SearchValue;

public class SearchResultModel<E extends Model> implements Comparable<SearchResultModel<E>>,Serializable{

	protected E instance;
	
	private EntityManager entityManager;
	private double matchPercent = 0;
	
	private Log log;
	
	// constructors
	public SearchResultModel(E instance) {
		this.instance = instance;
	}
	
	public SearchResultModel(E instance, EntityManager entityManager) {
		this.instance = instance;
		this.entityManager = entityManager;
	}
	
	//business logic
	@Override
	public String toString() {
		log.info("[toString]");
		if(instance instanceof SearchValue){
			Object model = ((SearchValue)instance).getModel(entityManager);
			log.info("[toString] model:#0",model);
			return model.toString();
		}
		return instance.toString();
	}
	
	public int compareTo(SearchResultModel<E> obj){
		if(!(obj instanceof SearchResultModel))
			return 0;
		if(this.matchPercent==obj.matchPercent)//allow repeated values
			return 1;
		if(this.matchPercent<obj.matchPercent)
			return 1;
		return -1;
	}
	/**
	 * computes the match value/percent taking in consideration the total length of searchText
	 * and the length of matched words
	 * @param searchText
	 */
	public void computeMatchPercent(String searchText){
		if(searchText==null)
			return;
		String[] searchList = searchText.split(" ");
		this.computeMatchPercent(searchList);
	}
	
	public void computeMatchPercent(String[] searchList){
		int charCount = 0;
		
		//trim strings and count characters
		for(int i=0;i<searchList.length;i++){
			searchList[i] = searchList[i].trim();
			charCount += searchList[i].length();
		}
		this.computeMatchPercent(searchList, charCount);
	}
	public void computeMatchPercent(String[] trimmedList,int charCount){
		int score=0;
		for(String searchTxt:trimmedList){
			List<KeywordsLang> keywords = entityManager.createQuery("select m from KeywordsLang m " +
					"where m.searchValue.objectId=? and lower(m.keys) like ?")
					.setParameter(1, instance.getId())
					.setParameter(2, this.toSearchRegExp(searchTxt))
					.getResultList();
			if(keywords.size()>0)
				score+=searchTxt.length();
		}
		this.matchPercent = 100*score/charCount;
		//if(this.matchPercent>100)
			//this.matchPercent = 100;
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
	
	//getters and setters
	public E getInstance() {
		return instance;
	}

	public void setInstance(E instance) {
		this.instance = instance;
	}
	
	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public double getMatchPercent() {
		return matchPercent;
	}

	public void setMatchPercent(double matchPercent) {
		this.matchPercent = matchPercent;
	}
	
	public Log getLog() {
		return log;
	}

	public void setLog(Log log) {
		this.log = log;
	}
}
