package com.obc.csrg.report;

import javax.ejb.Remove;
import javax.ejb.Stateful;

import java.io.Serializable;
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import com.obc.csrg.constants.SearchObjectTypeEnum;
import com.obc.csrg.events.ModelEvent;
import com.obc.csrg.events.ModelEventAdapter;
import com.obc.csrg.events.ModelEventQueue;
import com.obc.csrg.local.report.ModelDisplayInfoReportBeanLocal;
import com.obc.csrg.model.Model;
import com.obc.csrg.model.ModelDisplayInfo;
import com.obc.csrg.model.SearchObject;
import com.obc.csrg.model.SearchProperty;
import com.obc.csrg.model.PropertyDisplayInfo;

@Name("modelDisplayInfoReport")
@Stateful
@Scope(ScopeType.SESSION)
public class ModelDisplayInfoReportBean extends ReportBean<ModelDisplayInfo> implements ModelDisplayInfoReportBeanLocal{
	
	@Override
	public void create(){
		super.create();
		this.registerModelListener();
		//this.setDescriptionSearchTxt(messages.get("ReportDescSearchTxtTipoOferta"));
	}
	
	@Remove
	@Override
	public void destroy() {
		super.destroy();
		this.unregisterModelListener();
	}
	
	@Override
	public String query(){
		this.checkForNewModels();
		
		this.setBaseClassname("ModelDisplayInfo");
		this.setOrder("obj.name");
		this.setHibernateCacheable(false);
		this.setReportCacheable(false);
		String result = super.query();
		//log.info("[query] result:#0", super.getResultList());
		return result;
	}
	
	@Override
	public void createFilter(){
		//creation of initial filters including free filters if defined
		super.createFilter();

	}
	/**
	 * Searches in SearchModel for new models not currently in ModelDisplayInfo objects
	 */
	private void checkForNewModels(){
		List<SearchObject> newModels = entityManager.createQuery("select m from SearchObject m " +
				"where m.objectType=? and m.objectName not in (" +
				"select mdi.name from ModelDisplayInfo mdi)")
				.setParameter(1, SearchObjectTypeEnum.MODEL)
				.getResultList();
		if(newModels.size()>0){
			for(SearchObject model:newModels){
				log.info("[checkForNewModels] model:#0", model.getObjectName());
				ModelDisplayInfo mdi = new ModelDisplayInfo();
				mdi.setName(model.getObjectName());
				mdi.setAlias(model.getObjectName());
				entityManager.persist(mdi);
				//now, create properties
				this.createModelProperties(mdi, model);
			}
			entityManager.flush();
		}
	}
	/**
	 * create PropertyDisplayInfo objects for ModelDisplayInfo object
	 * @param mdi represents MdoleDisplayInfo object
	 * @param model the search object that originated mdi. It also contains properties
	 */
	private void createModelProperties(ModelDisplayInfo mdi,SearchObject model){
		int sortOrder = 1;
		for(SearchProperty prop:model.getProperties()){
			if(!prop.getDataType().equals("Set")){
				PropertyDisplayInfo p = new PropertyDisplayInfo();
				
				p.setName(prop.getName());
				p.setAlias(prop.getName());
				p.setSortOrder(sortOrder++);
				p.setDataType(prop.getDataType());
				p.setLocale(p.getLocale());
				p.setModel(mdi);
				
				entityManager.persist(p);
				mdi.getProperties().add(p);
			}
		}
	}
	
	// Model events handling
	private final class ModelEventHandler extends ModelEventAdapter implements
			Serializable {
		@Override
		public void newModel(ModelEvent e){
			Model m = (Model) e.getSource();
			if(m instanceof ModelDisplayInfo){
				setQueryOutdated(true);
			}
		}
		@Override
		public void modelUpdated(ModelEvent e){
			Model m = (Model) e.getSource();
			if(m instanceof ModelDisplayInfo){
				log.info("[modelUpdated] ModelDisplayInfo");
				setQueryOutdated(true);
			}
		}
		@Override
		public void onAfterRemove(ModelEvent e){
			Model m = (Model) e.getSource();
			if(m instanceof ModelDisplayInfo){
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
