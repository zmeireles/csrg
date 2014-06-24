package com.obc.csrg.report;

import javax.ejb.Remove;
import javax.ejb.Stateful;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import com.obc.csrg.constants.SearchObjectTypeEnum;
import com.obc.csrg.events.ModelEvent;
import com.obc.csrg.events.ModelEventAdapter;
import com.obc.csrg.events.ModelEventQueue;
import com.obc.csrg.local.report.DataChangeModelReportBeanLocal;
import com.obc.csrg.model.Model;
import com.obc.csrg.model.DataChangeModel;
import com.obc.csrg.model.SearchObject;
import com.obc.csrg.model.SearchProperty;
import com.obc.csrg.model.DataChangeProperty;
import com.obc.csrg.search.KeywordsFactory;

@Name("dataChangeModelReport")
@Stateful
@Scope(ScopeType.SESSION)
public class DataChangeModelReportBean extends ReportBean<DataChangeModel> implements DataChangeModelReportBeanLocal{
	
	@Override
	public void create(){
		super.create();
		this.checkForNewModels();
		this.updateExistingModelsProperties();
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
		
		this.setBaseClassname("DataChangeModel");
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
	 * Searches in SearchModel for new models not currently in DataChangeModel objects
	 */
	private void checkForNewModels(){
		List<SearchObject> newModels = entityManager.createQuery("select m from SearchObject m " +
				"where m.objectType=? and m.objectName not in (" +
				"select dcm.name from DataChangeModel dcm)")
				.setParameter(1, SearchObjectTypeEnum.MODEL)
				.getResultList();
		if(newModels.size()>0){
			for(SearchObject model:newModels){
				log.info("[checkForNewModels] model:#0", model.getObjectName());
				DataChangeModel mdi = new DataChangeModel();
				mdi.setAlias(model.getObjectName());
				mdi.setName(model.getObjectName());
				entityManager.persist(mdi);
				//now, create properties
				this.createModelProperties(mdi, model);
			}
			entityManager.flush();
		}
	}
	/**
	 * create DataChangeProperty objects for DataChangeModel object
	 * @param dcm represents DataChangeModel object
	 * @param model the search object that originated dcm. It also contains properties
	 */
	private void createModelProperties(DataChangeModel dcm,SearchObject model){
		for(SearchProperty prop:model.getProperties()){
			if(!prop.getDataType().equals("Set")){
				DataChangeProperty p = new DataChangeProperty();
				
				p.setAlias(prop.getName());
				p.setName(prop.getName());
				p.setDataType(prop.getDataType());
				p.setLocale(p.getLocale());
				p.setDataChangeModel(dcm);
				
				entityManager.persist(p);
				dcm.getProperties().add(p);
			}
		}
	}
	/**
	 * iterates in already existing models and check for changes in properties (new properties)
	 * Adds new properties and remove unused ones
	 * 
	 */
	private void updateExistingModelsProperties(){
		boolean result = false;
		List<SearchObject> newModels = entityManager.createQuery("select m from SearchObject m " +
				"where m.objectType=? and m.objectName in (" +
				"select dcm.name from DataChangeModel dcm)")
				.setParameter(1, SearchObjectTypeEnum.MODEL)
				.getResultList();
		if(newModels.size()>0){
			for(SearchObject model:newModels){
				List<DataChangeModel> dcms = entityManager.createQuery("select m from DataChangeModel m " +
						"where m.name=?")
						.setParameter(1, model.getObjectName())
						.getResultList();
				if(dcms.size()>0){
					if(!result)
						result = this.checkForNewProperties(dcms.get(0), model);
					else
						this.checkForNewProperties(dcms.get(0), model);
				}
			}
		}
		if(result){
			log.info("[updateExistingModels] flushing...");
			entityManager.flush();
		}
	}
	
	private boolean checkForNewProperties(DataChangeModel dcm,SearchObject model){
		boolean result=false;
		List<DataChangeProperty> addedProperties = new ArrayList<DataChangeProperty>();
		for(SearchProperty sp:model.getProperties()){
			if(!sp.getDataType().equals("Set")){
				boolean exist = false;
				for(DataChangeProperty p:dcm.getProperties()){
					if(p.getName().equals(sp.getName())){
						exist=true;
						break;
					}
				}
				//if(dcm.getName().equals("Person")) log.info("[checkForNewProperties] property:#0, exist:#1", sp.getName(),exist);
				if(!exist){
					DataChangeProperty p = new DataChangeProperty();
					p.setAlias(sp.getName());
					p.setName(sp.getName());
					p.setDataType(sp.getDataType());
					p.setLocale(p.getLocale());
					p.setDataChangeModel(dcm);
					entityManager.persist(p);
					addedProperties.add(p);
					//if(dcm.getName().equals("Person")) log.info("[checkForNewProperties] property created, id:#0",p.getId());
					result = true;
				}
			}
		}
		for(DataChangeProperty p:addedProperties){
			dcm.getProperties().add(p);
			//if(dcm.getName().equals("Person")) log.info("checkForNewProperties] added property:#0", p);
			entityManager.merge(dcm);
		}
		if(!result)
			result = this.removeUnusedProperties(dcm, model);
		else
			this.removeUnusedProperties(dcm, model);
		return result;
	}
	private boolean removeUnusedProperties(DataChangeModel dcm,SearchObject model){
		boolean result = false;
		List<DataChangeProperty> removedProperties = new ArrayList<DataChangeProperty>();
		for(DataChangeProperty p:dcm.getProperties()){
			boolean exist = false;
			for(SearchProperty sp:model.getProperties()){
				if(!sp.getDataType().equals("Set")){
					if(p.getName().equals(sp.getName())){
						exist=true;
						break;
					}
				}
			}
			if(!exist){
				removedProperties.add(p);
				result = true;
			}
		}
		for(DataChangeProperty p:removedProperties){
			dcm.getProperties().remove(p);
			p.setDataChangeModel(null);
			log.info("[removeUnusedProperties] remove property:#0 from model:#1", p,dcm);
			entityManager.remove(p);
		}
		return result;
	}
	// Model events handling
	private final class ModelEventHandler extends ModelEventAdapter implements
			Serializable {
		@Override
		public void newModel(ModelEvent e){
			Model m = (Model) e.getSource();
			if(m instanceof DataChangeModel){
				setQueryOutdated(true);
			}
		}
		@Override
		public void modelUpdated(ModelEvent e){
			Model m = (Model) e.getSource();
			if(m instanceof DataChangeModel){
				log.info("[modelUpdated] DataChangeModel");
				setQueryOutdated(true);
			}
		}
		@Override
		public void onAfterRemove(ModelEvent e){
			Model m = (Model) e.getSource();
			if(m instanceof DataChangeModel){
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
