package com.obc.csrg.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.obc.csrg.constants.ContextKeywordEnum;

public class ContextCount implements Serializable {

	private ContextKeywordEnum contexto;
	private double count = 0.0;
	private List<String> keywords = new ArrayList<String>();
	private double maxScore = 0.0;
	
	//constructors
	public ContextCount(ContextKeywordEnum contexto,int count){
		this.contexto = contexto;
		this.count = count;
	}
	//business methods
	public double getPercentage(){
		double percentage=0;
		if(this.count==0)
			return 0;
		if(this.maxScore==0)
			return 100;
		percentage = 100 * (this.count/this.maxScore);
		long val = Math.round(percentage*100);
		percentage = val/100;
		//if(percentage>100)
		//	percentage=100;
		return percentage;
	}
	//getters and setters
	public ContextKeywordEnum getContexto() {
		return contexto;
	}
	public void setContexto(ContextKeywordEnum contexto) {
		this.contexto = contexto;
	}
	public double getCount() {
		return count;
	}
	public void setCount(double count) {
		this.count = count;
	}
	public List<String> getKeywords() {
		return keywords;
	}
	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}
	public double getMaxScore() {
		return maxScore;
	}
	public void setMaxScore(double maxScore) {
		this.maxScore = maxScore;
	}
}
