package com.obc.csrg.action;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

import com.obc.csrg.local.CreateKeywordsActionLocal;
import com.obc.csrg.local.GeneralOperationsLocal;
import com.obc.csrg.constants.ModelEnum;

@Stateless
@Name("generalOperationsAction")
//@Scope(ScopeType.PAGE)
public class GeneralOperationsAction implements Serializable, GeneralOperationsLocal{

	@Logger
	private Log log;
	
	@In(create=true)
	private CreateKeywordsActionLocal createKeywordsAction;
	
	@In
	private Map<String, String> messages;
	
	@In
	private EntityManager entityManager;
	
	private List<SelectItem> modelsList = new ArrayList<SelectItem>();
	
	private ModelEnum selectedModel=ModelEnum.ALL_MODELS;
	
	// business functions
	/**
	 * create keywords for all model objects
	 */
	public void createKeywords(){
		createKeywordsAction.createAllModelsKeywords(selectedModel);
		
	}
	public void clearKeywords(){
		createKeywordsAction.clearModelKeywords(selectedModel);
	}
	/**
	 * update properties for all models
	 */
	public void updateProperties(){
		createKeywordsAction.updateProperties(selectedModel);
	}
	private void initModelsList(){
		modelsList.clear();
		for(ModelEnum mu:ModelEnum.values()){
			modelsList.add(new SelectItem(mu,messages.get(mu.getName())));
		}
	}
	
	//getters and setters
	
	public List<SelectItem> getModelsList() {
		if(this.modelsList.size()==0)
			this.initModelsList();
		return modelsList;
	}

	public void setModelsList(List<SelectItem> modelsList) {
		this.modelsList = modelsList;
	}
	
	public ModelEnum getSelectedModel() {
		return selectedModel;
	}

	public void setSelectedModel(ModelEnum selectedModel) {
		this.selectedModel = selectedModel;
	}
}
