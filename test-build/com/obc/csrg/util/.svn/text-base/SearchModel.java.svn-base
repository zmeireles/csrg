package com.obc.csrg.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.io.Serializable;

import com.obc.csrg.model.Person;

public class SearchModel implements Comparable<SearchModel>,Serializable{
	
	private static final long serialVersionUID = 200904201655L;
	
	private double matchValue=0;
	private String searchTxt="";
	private Person person;
	private List<ContextCount> contextHits = new ArrayList<ContextCount>();
	private double matchPercent=0;
	private double maxValue=0;
	private String matchReport = ""; //usado para justificar os valores dados
	
	//constructors
	public SearchModel(){
		
	}
	
	
	//end of constructors
	//Business logic
	/**
	 * computes the match value and the percentage of match based in the maxValue
	 */
	public void computeMatchValue(){
		double total=0;
		for(ContextCount c:this.contextHits){
			total += c.getCount() * 1;//avaliacao.devolveContextoAvaliacaoEnum(c.getContexto()).getWeight(); // c.getContexto().getWeight();
		}
		this.matchValue = total;
		//calculate
		if(this.maxValue>0)
			this.matchPercent = 100 * (this.matchValue/this.maxValue);
		else
			this.matchPercent = 100;
		
		if(this.matchPercent>100)
			this.matchPercent = 100;
		long val = Math.round(this.matchPercent*100);
		this.matchPercent = val/100;
		//build the report
		//this.matchReport = this.buildMatchReport();
	}
	/**
	 * resets all values from contextHits to zero
	 */
	public void resetMatchValue(){
		for(ContextCount c:this.contextHits){
			c.setCount(0);
		}
	}
	/**
	 * based on the context collection, builds a string with the contexts and respective score
	 * @return the justification of the achieved score, context by context in HTML form
	 */
	public String buildMatchReport(final Map<String,String> messages){
		StringBuffer header = new StringBuffer().append("<table border='0' cellspacing='1' cellpadding='0'>")
									.append("<tr><th align='left'>")
									.append(messages.get("ContextoName"))
									.append("</th><th align='left'>")
									.append(messages.get("ContextoPercentScore"))
									.append("</th>");
		StringBuffer footer = new StringBuffer().append("</table>");
		StringBuffer result = new StringBuffer();
		
		for(ContextCount c:this.contextHits){
			if(c.getCount()>0){
				String line = messages.get(c.getContexto().getName()) + "</td><td align='right'>" + 
								this.getPercentagem(c.getCount()*1);//avaliacao.devolveContextoAvaliacaoEnum(c.getContexto()).getWeight()); // c.getContexto().getWeight());
				//String line = messages.get(c.getContexto().getName()) + "</td><td align='right'>" + c.getPercentage();
				result.append("<tr><td align='left'>").append(line).append("</td></tr>");
			}
		}
		this.matchReport = new StringBuffer()
								.append(header.toString())
								.append(result.toString())
								.append(footer.toString()).toString();
		return this.matchReport;
	}
	public double getPercentagem(double valor){
		double percentagem = 0.0;
		if(this.maxValue>0)
			percentagem = 100 *(valor/this.maxValue);
		else
			percentagem = 100;
		long val = Math.round(percentagem * 100);
		return val/100;
	}
	//getters and setters
	public double getMatchValue() {
		return matchValue;
	}

	public void setMatchValue(double matchValue) {
		this.matchValue = matchValue;
	}

	public int compareTo(SearchModel obj){
		if(!(obj instanceof SearchModel))
			return 0;
		SearchModel sm = (SearchModel) obj;
		if(this.matchPercent==sm.matchPercent)//permitir valores repetidos
			return 1;
		if(this.matchPercent<sm.matchPercent)
			return 1;
		return -1;
	}
	public String getSearchTxt() {
		return searchTxt;
	}
	public void setSearchTxt(String searchTxt) {
		this.searchTxt = searchTxt;
	}
	public Person getPerson() {
		return person;
	}
	public void setPerson(Person person) {
		this.person = person;
	}
	public List<ContextCount> getContextHits() {
		return contextHits;
	}
	public void setContextHits(List<ContextCount> contextHits) {
		this.contextHits = contextHits;
	}
	public double getMatchPercent() {
		return matchPercent;
	}
	public void setMatchPercent(double matchPercent) {
		this.matchPercent = matchPercent;
	}
	public double getMaxValue() {
		return maxValue;
	}
	public void setMaxValue(double maxValue) {
		this.maxValue = maxValue;
	}
	public String getMatchReport() {
		return matchReport;
	}
	public void setMatchReport(String matchReport) {
		this.matchReport = matchReport;
	}
	
}
