package com.obc.csrg.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ActionEvent;


import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.web.RequestParameter;

import com.obc.csrg.events.ModelEvent;
import com.obc.csrg.events.ModelEventAdapter;
import com.obc.csrg.events.ModelEventQueue;
import com.obc.csrg.model.DataChangeModel;
import com.obc.csrg.model.Model;
import com.obc.csrg.model.DataChangeProperty;
import com.obc.csrg.util.DataChangeUtil;

@Name("dataChangeModelHome")
public class DataChangeModelHome<E extends Model> extends ObcEntityHome<DataChangeModel> {

	@RequestParameter
	Long dataChangeModelId;
	
	private List<DataChangeProperty> properties = new ArrayList<DataChangeProperty>();

	@Override
	public Object getId() {
		if (dataChangeModelId == null)
			return super.getId();
		else
			return dataChangeModelId;
	}

	@Override
	@Create
	@Begin(join = true)
	public void create() {
		super.create();
		log.info("[create] dataChangeModelHome created");
		this.registerModelListener();	
		//this.initProperties();
	}

	@Override
	@Destroy
	public void destroy() {
		super.destroy();
		this.unregisterModelListener();
	}

	@Override
	protected void loadAfterCreate() {
		super.loadAfterCreate();
		
		//dataChange = new DataChangeUtil<DataChangeModel>(this.instance,entityManager,log,stateBean.getUser());
		//dataChange.replicateValues();
		this.initDefaultMessages();
		this.initProperties();
	}
	
	private void initProperties(){
		this.properties.clear();
		List<DataChangeProperty> props = entityManager.createQuery("select m from DataChangeProperty m " +
				"where m.dataChangeModel.id=? order by m.alias")
				.setParameter(1, instance.getId())
				.getResultList();
		log.info("[initProperties] size:#0", props.size());
		for(DataChangeProperty p:props){
			properties.add(p);
		}
	}
	
	// Model events handling
	private final class ModelEventHandler extends ModelEventAdapter implements Serializable {
		@Override
		public void newModel(ModelEvent e) {
			Model m = (Model) e.getSource();
		}

		@Override
		public void onBeforeRemove(ModelEvent e) {
			Model m = (Model) e.getSource();
		}
	}

	private void registerModelListener() {
		log.info("[registerModelListener]");
		this.modelEventListener = new ModelEventHandler();
		ModelEventQueue.addMsgListener(this.modelEventListener);
	}

	private void unregisterModelListener() {
		if (this.modelEventListener != null) {
			log.info("[unregisterModelListener]");
			ModelEventQueue.removeMsgListener(this.modelEventListener);
		}
	}
	
	public void saveProperty(ActionEvent event){
		Long propertyId = (Long)event.getComponent().getAttributes().get("propertyId");
		log.info("[saveProperty] id:#0", propertyId);
		for(DataChangeProperty p:this.properties){
			if(p.getId().equals(propertyId)){
				entityManager.merge(p);
				break;
			}
		}
	}
	
	// getters and setters
	
	public List<DataChangeProperty> getProperties() {
		return properties;
	}

	public void setProperties(List<DataChangeProperty> properties) {
		this.properties = properties;
	}
	
}
