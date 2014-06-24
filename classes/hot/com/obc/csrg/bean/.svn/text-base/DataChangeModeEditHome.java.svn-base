package com.obc.csrg.bean;


import javax.faces.event.ValueChangeEvent;

import org.jboss.seam.annotations.Name;

import com.obc.csrg.model.DataChangeModel;
import com.obc.csrg.model.DataChangeProperty;
import com.obc.csrg.model.DataChangeValue;
import com.obc.csrg.model.Config;
import com.obc.csrg.util.EmailUtil;

@Name("dataChangeModelEditHome")
public class DataChangeModeEditHome extends DataChangeModelHome<DataChangeModel> {

	@Override
	protected void loadAfterPersist(){
		super.loadAfterPersist();
		
	}
	protected void loadAfterUpdate(){
		super.loadAfterUpdate();
	}
	
	@Override
	protected boolean verifyData(){
		boolean result = false;
		//log.info("[verifyData] instance   alias:#0", this.instance.getAlias());
		//log.info("[verifyData] replicated alias:#0", ((DataChangeModel)dataChange.getReplication()).getAlias());
		
		if(super.verifyData()){
			boolean valid = true;
			
			if(valid) result = true;
		}
		return result;
	}
	
	public void valueChangeListener(ValueChangeEvent event){
		
		//log.info("[valueChangeListener] component id:#0", event.getComponent().getId());
		String componentId = event.getComponent().getId();
		if(componentId.equals("notifyForAnyPropertyChange")){
			//log.info("[valueChangeListener] changed notifyForAnyPropertyChange");
			boolean newValue = (Boolean)event.getNewValue();
			this.instance.setNotifyForAnyPropertyChange(newValue);
		}else if(componentId.equals("notifyOnChange")){
			DataChangeProperty prop = (DataChangeProperty)event.getComponent().getAttributes().get("property");
			if(prop!=null){
				boolean newValue = (Boolean)event.getNewValue();
				prop.setNotifyOnChange(newValue);
			}
		}
	}
	
}
