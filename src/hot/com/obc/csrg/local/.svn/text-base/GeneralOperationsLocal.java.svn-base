package com.obc.csrg.local;

import java.util.List;

import javax.ejb.Local;
import javax.faces.model.SelectItem;

import com.obc.csrg.constants.ModelEnum;

@Local
public interface GeneralOperationsLocal {

	public void createKeywords();
	public void clearKeywords();
	public void updateProperties();
	
	public List<SelectItem> getModelsList();
	public void setModelsList(List<SelectItem> modelsList);
	public ModelEnum getSelectedModel();
	public void setSelectedModel(ModelEnum selectedModel);
}
