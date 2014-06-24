package com.obc.csrg.report;

import java.io.Serializable;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.Query;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import com.obc.csrg.events.ModelEvent;
import com.obc.csrg.events.ModelEventAdapter;
import com.obc.csrg.events.ModelEventQueue;
import com.obc.csrg.local.report.GeographicAreaReportBeanLocal;
import com.obc.csrg.model.Model;
import com.obc.csrg.model.GeographicArea;
import com.obc.csrg.util.ReportFilter;

@Name("geographicAreaReport")
@Stateful
@Scope(ScopeType.SESSION)
public class GeographicAreaReportBean extends ReportBean<GeographicArea> implements GeographicAreaReportBeanLocal{
	
	@Create
	@Override
	public void create(){
		super.create();
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
		this.setBaseClassname("GeographicArea");
		this.setOrder("obj.name");
		//this.setHibernateCacheable(true);
		String result = super.query();
		//log.info("[query] result:#0", super.getResultList());
		return result;
	}
	
	@Override
	public void createFilter(){
		//creation of initial filters including free filters if defined
		super.createFilter();

	}
	
	// Model events handling
	private final class ModelEventHandler extends ModelEventAdapter implements
			Serializable {
		@Override
		public void newModel(ModelEvent e){
			Model m = (Model) e.getSource();
			if(m instanceof GeographicArea){
				setQueryOutdated(true);
			}
		}
		@Override
		public void modelUpdated(ModelEvent e){
			Model m = (Model) e.getSource();
			if(m instanceof GeographicArea){
				setQueryOutdated(true);
			}
		}
		@Override
		public void onAfterRemove(ModelEvent e){
			Model m = (Model) e.getSource();
			if(m instanceof GeographicArea){
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
}
