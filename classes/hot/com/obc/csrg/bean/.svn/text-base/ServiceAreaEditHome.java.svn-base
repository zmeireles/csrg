package com.obc.csrg.bean;

import org.jboss.seam.annotations.Name;

import com.obc.csrg.model.ServiceArea;

@Name("serviceAreaEditHome")
public class ServiceAreaEditHome extends ServiceAreaHome<ServiceArea> {

	
	@Override
	protected void loadAfterCreate(){
		super.loadAfterCreate();
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
