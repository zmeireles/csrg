package com.obc.csrg.bean;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.ejb.Stateless;

import java.util.List;
import java.util.ArrayList;

import javax.persistence.EntityManager;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.web.RequestParameter;
import org.jboss.seam.log.Log;

import java.io.Serializable;
import java.util.Map;

import com.obc.csrg.events.ModelEvent;
import com.obc.csrg.events.ModelEventAdapter;
import com.obc.csrg.events.ModelEventListener;
import com.obc.csrg.events.ModelEventQueue;
import com.obc.csrg.local.NewsPanelControllerLocal;
import com.obc.csrg.local.StateBeanLocal;
import com.obc.csrg.model.Model;
import com.obc.csrg.model.News;
import com.obc.csrg.model.User;

@Name("newsPanelController")
@Stateful
@Scope(ScopeType.SESSION)
public class NewsPanelController implements Serializable,NewsPanelControllerLocal {

	@In
	private Map<String, String> messages;
	
	@Logger 
	private Log log;
	
	@In 
    private EntityManager entityManager;
	
	@In(required=false)
	protected StateBeanLocal stateBean;
	
	@RequestParameter
	private Long newsId;
	
	private List<News> news = new ArrayList<News>();
	
	private int newsCount=0;
	
	private final String QUERY = "select m from News m where event=false and " + //exclude events
			"active=true and approved=true " + //active and approved status
			"and (beginDate is null or beginDate<=current_timestamp()) and " + //check for begin date
			"(endDate is null or endDate>=current_timestamp()) " + //check for end date 
			"and ((select count(u.id) from UserNotification u where u.news.id=m.id)=0 " + 	//everyone
			"or ? in (select u.user.id from UserNotification u where u.news.id is not null and u.news.id=m.id))" +	// or allowed user
			"order by beginDate desc";
	
	protected ModelEventListener modelEventListener;
	private boolean queryOutdated=true;
	//business functions
	
	@Begin(join=true)
	@Create
	public void create() {
		this.registerModelListener();
		query();
	}
	
	@Remove
	@Destroy
	public void destroy() {
		this.unregisterModelListener();
	}
	
	@Observer("org.jboss.seam.postCreate.stateBean")
	public void stateBeanCreated(){
		//log.info("[stateBeanCreated] userId:#0, stateBean:#1, user:#2",
			//	stateBean==null || stateBean.getUser()==null ? 0L: stateBean.getUser().getId(),
			//	stateBean,stateBean.getUser());
		query();
	}
	/*
	@Observer("org.jboss.seam.preDestroyContext.SESSION")
	public void sessionEnded(){
		log.info("[sessionEnded] userId:#1, stateBean:#0",stateBean,
				stateBean==null || stateBean.getUser()==null ? 0L: stateBean.getUser().getId());
	}
	*/
	@Observer("org.jboss.seam.security.loginSuccessful")
	public void loginSuccessful(){
		//log.info("[postAuthenticate] userId:#0, stateBean:#1, user:#2",
			//	stateBean==null || stateBean.getUser()==null ? 0L: stateBean.getUser().getId(),
			//	stateBean,stateBean.getUser());
		query();
	}
	private void query(){
		this.queryOutdated=false;
		if(log==null)
        	log = (Log) Component.getInstance("log");
		if(stateBean==null)
			stateBean = (StateBeanLocal) Component.getInstance("stateBean");
		
		//log.info("[query] userId:#0, stateBean:[#1]",stateBean==null || stateBean.getUser()==null ? 
			//	0L: stateBean.getUser().getId(),stateBean);
		if(entityManager==null)
        	entityManager = (EntityManager) Component.getInstance("entityManager");
		news = entityManager.createQuery(QUERY)
				.setParameter(1, stateBean==null || stateBean.getUser()==null ? 0L: stateBean.getUser().getId())
				.getResultList();
		//log.info("[query] results:#0", news.size());
	}
	
	// Model events handling
	private final class ModelEventHandler extends ModelEventAdapter implements
			Serializable {
		@Override
		public void newModel(ModelEvent e){
			Model m = (Model) e.getSource();
			if(m instanceof News){
				if(log==null)
		        	log = (Log) Component.getInstance("log");
				//log.info("[newModel] news set queryOutdated to true");
				queryOutdated=true;
			}
		}
		@Override
		public void modelUpdated(ModelEvent e){
			Model m = (Model) e.getSource();
			if(m instanceof News){
				if(log==null)
		        	log = (Log) Component.getInstance("log");
				//log.info("[newModel] news set queryOutdated to true");
				queryOutdated=true;
			}
		}
		@Override
		public void onAfterRemove(ModelEvent e){
			Model m = (Model) e.getSource();
			if(m instanceof News){
				if(log==null)
		        	log = (Log) Component.getInstance("log");
				//log.info("[newModel] news set queryOutdated to true");
				queryOutdated=true;
			}
		}
	}
    
	private void registerModelListener() {
		//log.info("[registerModelListener]");
		this.unregisterModelListener();
		this.modelEventListener = new ModelEventHandler();
		ModelEventQueue.addMsgListener(this.modelEventListener);
	}

	private void unregisterModelListener() {
		if (this.modelEventListener != null) {
			//log.info("[unregisterModelListener]");
			ModelEventQueue.removeMsgListener(this.modelEventListener);
		}
	}
	
	public String viewNews(){
		//log.info("[viewNews] newsId:#0", newsId);
		return "";
	}
	//getters and setters
	
	public List<News> getNews() {
		if(this.queryOutdated)
			query();
		return news;
	}

	public void setNews(List<News> news) {
		this.news = news;
	}
	public int getNewsCount() {
		if(queryOutdated)
			query();
		newsCount = news.size();
		return newsCount;
	}

	public void setNewsCount(int newsCount) {
		this.newsCount = newsCount;
	}
	
	public boolean isQueryOutdated() {
		return queryOutdated;
	}

	public void setQueryOutdated(boolean queryOutdated) {
		this.queryOutdated = queryOutdated;
	}
}
