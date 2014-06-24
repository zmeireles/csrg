package com.obc.csrg.bean;

import org.jboss.seam.annotations.Name;

import com.obc.csrg.model.ProfCategory;

@Name("profCategoryEditHome")
public class ProfCategoryEditHome extends ProfCategoryHome<ProfCategory> {

	
	@Override
	protected void loadAfterUpdate(){
		super.loadAfterUpdate();
	}
	@Override
	protected void loadAfterPersist(){
		super.loadAfterPersist();
	}
	
	@Override
	protected boolean verifyData(){
		boolean result = false;
		if(super.verifyData()){
			boolean valid = true;
			if(this.instance.getLocale()==null || this.instance.getLocale().equals(""))
				this.instance.setLocale(locale.getLanguage());
			if(valid) result = true;
		}
		return result;
	}

}
