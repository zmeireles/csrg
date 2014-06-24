package com.obc.csrg.report;

import java.io.Serializable;

import javax.ejb.Remove;
import javax.ejb.Stateful;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import com.obc.csrg.action.VisualItemMenuBean;
import com.obc.csrg.events.ModelEvent;
import com.obc.csrg.events.ModelEventAdapter;
import com.obc.csrg.events.ModelEventQueue;
import com.obc.csrg.local.report.VisualItemReportBeanLocal;
import com.obc.csrg.model.Model;
import com.obc.csrg.model.VisualItem;
import com.obc.csrg.util.ReportFilter;

@Name("visualItemReport")
@Stateful
@Scope(ScopeType.SESSION)
public class VisualItemReportBean extends ReportBean<VisualItem> implements VisualItemReportBeanLocal{
	

	private VisualItemMenuBean menu = new VisualItemMenuBean(); //this is added for the dynamic menu test
	
	//private VisualItemMenuBean menu2;
	
	@Override
	public void create(){
		super.create();
		this.setDescriptionSearchTxt(messages.get("ReportDescSearchTxtTipoOferta"));
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
		this.setBaseClassname("VisualItem");
		this.setOrder("obj.menuText");
		//this.setHibernateCacheable(true);
		String result = super.query();
		//log.info("[query] result:#0", super.getResultList());
		return result;
	}
	
	@Override
	public void createFilter(){
		
		super.createFilter();

	}
	
	// Model events handling
	private final class ModelEventHandler extends ModelEventAdapter implements
			Serializable {
		@Override
		public void newModel(ModelEvent e){
			Model m = (Model) e.getSource();
			if(m instanceof VisualItem){
				setQueryOutdated(true);
			}
		}
		@Override
		public void modelUpdated(ModelEvent e){
			Model m = (Model) e.getSource();
			if(m instanceof VisualItem){
				setQueryOutdated(true);
			}
		}
		@Override
		public void onAfterRemove(ModelEvent e){
			Model m = (Model) e.getSource();
			if(m instanceof VisualItem){
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
	
	//this is added for the dynamic menu test
	public void setMenu(VisualItemMenuBean menu) {
		this.menu = menu;
	}
	
	//this is added for the dynamic menu test
	public VisualItemMenuBean getMenu() {
		//menu.createMenu();
		return menu;
	}


	
}
