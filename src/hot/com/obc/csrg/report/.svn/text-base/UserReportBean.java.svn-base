package com.obc.csrg.report;

import java.io.Serializable;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.ejb.Remove;
import javax.ejb.Stateful;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import com.obc.csrg.events.ModelEvent;
import com.obc.csrg.events.ModelEventAdapter;
import com.obc.csrg.events.ModelEventQueue;
import com.obc.csrg.local.report.UserReportBeanLocal;
import com.obc.csrg.model.Person;
import com.obc.csrg.model.Model;
import com.obc.csrg.model.News;
import com.obc.csrg.model.User;
import com.obc.csrg.search.KeywordsFactory;
import com.obc.csrg.util.ReportFilter;

@Name("userReport")
@Stateful
@Scope(ScopeType.SESSION)
public class UserReportBean extends ReportBean<User> implements UserReportBeanLocal{
	
	@In
	protected Map<String,String> messages;
	
	private int activeStatus=0;
	
	private List<SelectItem> activeStatusItems = new ArrayList<SelectItem>();
	
	@Override
	public void create(){
		super.create();
		this.initActiveStatusList();
		//this.setDescriptionSearchTxt(messages.get("ReportDescSearchTxtTipoOferta"));
		this.registerModelListener();
	}
	
	@Remove
	@Override
	public void destroy() {
		super.destroy();
		this.unregisterModelListener();
	}
	
	@Override
	public String query(){
		
		this.setBaseClassname("User");
		this.setOrder("obj.person.name");
		this.setHibernateCacheable(true);
		String result = super.query();
		//log.info("[query] result:#0", super.getResultList());
		return result;
	}
	
	@Override
	public void createFilter(){
		//creation of initial filters including free filters if defined
		
		//log.info("[createFilter] activeStatus:#0",this.activeStatus);
		super.createFilter();
		
		//specific filters
		if(this.activeStatus!=0){
			ReportFilter filterToAdd = new ReportFilter();
			if(this.activeStatus==1)//active users
				filterToAdd.addFilter("obj.active=?", true);
			else if(this.activeStatus==2)//inactive users
				filterToAdd.addFilter("obj.active=?", false);
			//join filters
			this.addFilter(filterToAdd);
		}
	}
	// Model events handling
	private final class ModelEventHandler extends ModelEventAdapter implements
			Serializable {
		@Override
		public void newModel(ModelEvent e){
			Model m = (Model) e.getSource();
			if(m instanceof News){
				setQueryOutdated(true);
			}
		}
		@Override
		public void modelUpdated(ModelEvent e){
			Model m = (Model) e.getSource();
			if(m instanceof News){
				setQueryOutdated(true);
			}
		}
		@Override
		public void onAfterRemove(ModelEvent e){
			Model m = (Model) e.getSource();
			if(m instanceof News){
				setQueryOutdated(true);
			}
		}
	}
	
	private void registerModelListener() {
		log.info("[registerModelListener]");
		this.unregisterModelListener();
		this.modelEventListener = new ModelEventHandler();
		ModelEventQueue.addMsgListener(this.modelEventListener);
	}

	private void unregisterModelListener() {
		if (this.modelEventListener != null) {
			log.info("[unregisterModelListener]");
			ModelEventQueue.removeMsgListener(this.modelEventListener);
		}
	}
	
	private void initActiveStatusList(){
		this.activeStatusItems.clear();
		this.activeStatusItems.add(new SelectItem(0,messages.get("SelectAllShort")));
		this.activeStatusItems.add(new SelectItem(1,messages.get("ActiveUsers")));
		this.activeStatusItems.add(new SelectItem(2,messages.get("InactiveUsers")));
	}
	
	public void activeStatusListener(ValueChangeEvent event) {
		this.activeStatus = (Integer)event.getNewValue();
		//log.info("[activeStatusListener] activeStatus:#0", this.activeStatus);
		this.query();
	}
	
	//Getters and setters
	
	public int getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(int activeStatus) {
		this.activeStatus = activeStatus;
	}
	
	public List<SelectItem> getActiveStatusItems() {
		return activeStatusItems;
	}

	public void setActiveStatusItems(List<SelectItem> activeStatusItems) {
		this.activeStatusItems = activeStatusItems;
	}

}
