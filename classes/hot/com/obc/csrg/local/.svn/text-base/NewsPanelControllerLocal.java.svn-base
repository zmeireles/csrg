package com.obc.csrg.local;

import java.util.List;

import javax.ejb.Local;
import javax.faces.event.ActionEvent;

import com.obc.csrg.model.News;
import com.obc.csrg.model.User;

@Local
public interface NewsPanelControllerLocal {

	public void create();
	public void destroy();
	public List<News> getNews();
	public void setNews(List<News> news);
	public String viewNews();
	public void stateBeanCreated();
	public void loginSuccessful();
	//public void sessionEnded();
	public int getNewsCount();
}
