package com.obc.csrg.action;

import javax.ejb.Stateless;
import javax.faces.context.FacesContext;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.List;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.web.RequestParameter;
import org.jboss.seam.log.Log;

import javax.persistence.EntityManager;

import com.obc.csrg.local.StateBeanLocal;
import com.obc.csrg.local.NewsTickerBeanLocal;
import com.obc.csrg.model.News;

@Stateless
@Name("newsTicker")
public class NewsTickerBean  implements NewsTickerBeanLocal {

	@Logger
	private Log log;

	@In
	protected EntityManager entityManager;
    
	@In(required = false)
	private FacesContext facesContext;

	@In(required=false)
	protected StateBeanLocal stateBean;
	
	@RequestParameter
	private Long pageId;
	
	String newsTickerTxt = "This will be the news ticker code";

	StringBuffer text= new StringBuffer();
	
	//this query is copied from NewsPanelController.java
	private final String QUERY = "select m from News m where event=false and " + //exclude events
	"active=true and approved=true " + //active and approved status
	"and highlight=true "+ //if true then it is a news ticker item
	"and (beginDate is null or beginDate<=current_timestamp()) and " + //check for begin date
	"(endDate is null or endDate>=current_timestamp()) " + //check for end date 
	"and ((select count(u.id) from UserNotification u where u.news.id=m.id)=0 " + 	//everyone
	"or ? in (select u.user.id from UserNotification u where u.news.id is not null and u.news.id=m.id))" +	// or allowed user
	"order by beginDate desc";
 
    public NewsTickerBean(){ 
    	this.newsTickerTxt = "some text";
    }
    
    public String buildNewsTicker(){
    	this.newsTickerTxt = "some text";
    	
    	//log.info("[buildMenu] menuTxt:#0", menuTxt);
    	//this.createMenu();
    	
    	//return this.menuTxt;
    	int i = this.text.length();
    	this.text.delete(0, i);
    	this.initNewsTicker();
    	//log.info("[buildMenu] menuTxt:#0", menuTxt);
    	//log.info("[buildMenu] text:#0", text.toString());
    	return text.toString();
    }
    
    public String getIFramePageLink(){
    	return "/csrg/forms/newsView.seam?newsId=" + pageId;
    }
    
   
    
/* ******************************************************************************************* */
    //Please check following 4 methods
    
    public void initNewsTicker(){
		
    	String html = "<ul id='topNewsBar'>\n";
		
    	this.text.append(html);
    	
		List<News> newsTickerItem = this.retrieveNewsItems();
		
		this.createNewsTickerCode(newsTickerItem);
		
		html = "</ul>\n";
		
    	this.text.append(html);

	}
    
    public List<News> retrieveNewsItems(){
		 List<News> newsItems = entityManager.createQuery(QUERY)
				 .setParameter(1, stateBean==null || stateBean.getUser()==null ? 0L: stateBean.getUser().getId())
					.getResultList();
		 //log.info("[retrieveTopMenuItems] menuItems:#0", menuItems);
		return newsItems;
    }
    
	
	
	
    public void createNewsTickerCode(List<News> newsItems){
		 
    	String html = "";
    	
    	for (int i=0; i< newsItems.size(); i++){
    	
    		News item = newsItems.get(i);
    		String link = this.makeLink("", item.getId());
    	 
    		html = "<li><a href='" + link + "' >" + item.getTitle() + "</a>\n";
    		
    		this.text.append(html);
    		
    		html = "</li>\n";
    		this.text.append(html);	
    	} 
	}
    
   

    public String makeLink(String xhtmlFile, long newsId){
    	String link = "/csrg/forms/newsView.seam?newsId=" + newsId;//"http://localhost/csrg/mypage.seam?id=" + pageId;
    	
    	// this is wrong: if(xhtmlFile == "" || xhtmlFile == null ) the == compares objects or values IF objects are 
    	// of primitive types. That's not the case of String.
    	//in the if below the order of conditions is important
    	if(xhtmlFile==null || xhtmlFile.equals(""))
    		return link;
    	else
    	  return "/csrg/" + xhtmlFile; // "http://localhost/csrg/" + xhtmlFile;
    }
    
    
    
    
  /* **************************************************************************************** */  
    
		
	//getters and setters
	public String getNewsTickerTxt() {
		return newsTickerTxt;
	}
	public void setNewsTickerTxt(String newsTickerTxt) {
		this.newsTickerTxt = newsTickerTxt;
	}
	
}
