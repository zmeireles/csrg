package com.obc.csrg.local;

import javax.ejb.Local;

import org.jboss.seam.annotations.async.Asynchronous;

import com.obc.csrg.constants.ModelEnum;
import com.obc.csrg.model.Model;

@Local
public interface CreateKeywordsActionLocal {

	//@Asynchronous
	public void createModelKeywords(Model model);
	//@Asynchronous
	public void updateModelKeywords(Model model);
	//@Asynchronous
	public void removeModelKeywords(Model model);
	//@Asynchronous
	public void createAllModelsKeywords(ModelEnum model);
	//@Asynchronous
	public void clearModelKeywords(ModelEnum model);
	public void updateProperties(ModelEnum model);
	
	public boolean createKeywords4GeoAreas(int iteration,int records);
}
