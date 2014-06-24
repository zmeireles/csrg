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
import com.obc.csrg.model.ModelDisplayInfo;
import com.obc.csrg.model.Model;
import com.obc.csrg.model.PropertyDisplayInfo;
import com.obc.csrg.model.UrlProfileDisplayInfo;
import com.obc.csrg.model.Profile;

@Name("modelDisplayInfoHome")
public class ModelDisplayInfoHome<E extends Model> extends ObcEntityHome<ModelDisplayInfo> {

	@RequestParameter
	Long modelDisplayInfoId;
	
	private List<PropertyDisplayInfo> properties = new ArrayList<PropertyDisplayInfo>();
	private List<UrlProfileDisplayInfo> urls = new ArrayList<UrlProfileDisplayInfo>();

	@Override
	public Object getId() {
		if (modelDisplayInfoId == null)
			return super.getId();
		else
			return modelDisplayInfoId;
	}

	@Override
	@Create
	@Begin(join = true)
	public void create() {
		super.create();
		log.info("[create] modelDisplayInfoHome created");
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
		this.initDefaultMessages();
		this.verifyUrls();
		this.initProperties();
		this.initUrls();
	}
	
	private void initProperties(){
		this.properties.clear();
		List<PropertyDisplayInfo> props = entityManager.createQuery("select m from PropertyDisplayInfo m " +
				"where m.model.id=? order by m.sortOrder")
				.setParameter(1, instance.getId())
				.getResultList();
		log.info("[initProperties] size:#0", props.size());
		for(PropertyDisplayInfo p:props){
			properties.add(p);
		}
	}
	
	private void initUrls(){
		this.urls.clear();
		List<UrlProfileDisplayInfo> urls = entityManager.createQuery("select m from UrlProfileDisplayInfo m " +
				"where m.model.id=?")
				.setParameter(1, instance.getId())
				.getResultList();
		log.info("[initUrls] size:#0", urls.size());
		for(UrlProfileDisplayInfo p:urls){
			this.urls.add(p);
		}
	}
	/**
	 * verify if all profiles are contemplated in URLs, if not create missing ones
	 * also verify if the special URL wih no profile (for anonymous user) exists
	 */
	private void verifyUrls(){
		List<Profile> profiles = entityManager.createQuery("select m from Profile m")
										.getResultList();
		//log.info("[verifyUrls] profiles size:#0", profiles.size());
		for(Profile p:profiles){
			boolean profileExist=false;
			for(UrlProfileDisplayInfo url:this.getInstance().getUrls()){
				if(url.getProfile()!=null && url.getProfile().getType()==
					p.getType()){
					profileExist=true;
					break;
				}
			}
			if(!profileExist)
				this.createUrl(p);
		}
		//verify null profile URL
		boolean nullUrlExists=false;
		for(UrlProfileDisplayInfo url:this.getInstance().getUrls()){
			if(url.getProfile()==null){
				nullUrlExists=true;
				break;
			}
		}
		if(!nullUrlExists)
			this.createUrl(null);
		entityManager.flush();
		
		//log.info("[verifyUrls] urls size:#0", instance.getUrls().size());
	}
	private void createUrl(Profile p){
		//log.info("[createUrl] profile:#0", p);
		UrlProfileDisplayInfo url = new UrlProfileDisplayInfo();
		url.setProfile(p);
		url.setModel(instance);
		entityManager.persist(url);
		//log.info("[createUrl] url.profile:#0", url.getProfile());
		this.instance.getUrls().add(url);
		if(p!=null)
			p.getUrls().add(url);
	}

	// Model events handling
	private final class ModelEventHandler extends ModelEventAdapter implements
			Serializable {
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
		for(PropertyDisplayInfo p:this.properties){
			if(p.getId().equals(propertyId)){
				entityManager.merge(p);
				break;
			}
		}
	}
	
	public void saveUrl(ActionEvent event){
		Long urlId = (Long)event.getComponent().getAttributes().get("urlId");
		log.info("[saveUrl] id:#0", urlId);
		for(UrlProfileDisplayInfo p:this.urls){
			if(p.getId().equals(urlId)){
				entityManager.merge(p);
				break;
			}
		}
	}

	// getters and setters
	
	public List<PropertyDisplayInfo> getProperties() {
		return properties;
	}

	public void setProperties(List<PropertyDisplayInfo> properties) {
		this.properties = properties;
	}
	
	public List<UrlProfileDisplayInfo> getUrls() {
		return urls;
	}

	public void setUrls(List<UrlProfileDisplayInfo> urls) {
		this.urls = urls;
	}
}
