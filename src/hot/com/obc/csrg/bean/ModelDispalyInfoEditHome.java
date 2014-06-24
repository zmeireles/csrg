package com.obc.csrg.bean;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jboss.seam.annotations.Name;

import com.obc.csrg.model.ModelDisplayInfo;

@Name("modelDisplayInfoEditHome")
public class ModelDispalyInfoEditHome extends ModelDisplayInfoHome<ModelDisplayInfo> {

	@Override
	protected void loadAfterCreate(){
		super.loadAfterCreate();
		
	}
	protected void loadAfterUpdate(){
		super.loadAfterUpdate();
		
	}
	
	@Override
	protected boolean verifyData(){
		boolean result = false;
		if(super.verifyData()){
			boolean valid = true;
			
			if(valid) result = true;
		}
		return result;
	}
	
}
