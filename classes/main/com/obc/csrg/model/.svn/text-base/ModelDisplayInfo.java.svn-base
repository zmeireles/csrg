package com.obc.csrg.model;

import static javax.persistence.GenerationType.SEQUENCE;
import static org.jboss.seam.ScopeType.SESSION;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import com.obc.csrg.constants.Constants;
import com.obc.csrg.events.ModelEventQueue;

@Entity
@Name("modelDisplayInfo")
@Scope(SESSION)
@Table(name = "model_disp_info")
//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class ModelDisplayInfo extends Model implements Serializable{

	@SequenceGenerator(name = "generator")
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "model_disp_info_id", nullable = false)
	private Long id;

	private String locale=Constants.DEFAULT_LOCALE;
	
	private String name="";
	
	private String alias="";
	
	private String description="";
	
	@OneToMany(cascade = { CascadeType.MERGE,
			CascadeType.REMOVE }, fetch = FetchType.LAZY, mappedBy = "model")
	//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
	private Set<PropertyDisplayInfo> properties = new HashSet<PropertyDisplayInfo>(0);
	
	@OneToMany(cascade = { CascadeType.MERGE,
			CascadeType.REMOVE }, fetch = FetchType.LAZY, mappedBy = "model")
	//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
	private Set<UrlProfileDisplayInfo> urls = new HashSet<UrlProfileDisplayInfo>(0);
	
	//constructors
	public ModelDisplayInfo() {
	}

	//business logic

	@Override
	public String toString() {
		return name + ":" + alias + ":" + description;
	}
	
	@Override
	public boolean remove(EntityManager entityManager) {
		ModelEventQueue.fireOnBeforeModelRemoveEvent(this);
		
		entityManager.remove(this);
		ModelEventQueue.fireOnAfterModelRemoveEvent(this);
		return true;
	}
	public int hashCode() {
		return (this.id == null) ? 0 : this.id.hashCode();
	}

	public boolean equals(Object object) {
		if (object instanceof ModelDisplayInfo) {
			final ModelDisplayInfo obj = (ModelDisplayInfo) object;
			return (this.id != null) ? this.id.equals(obj.id)
					: (obj.id == null);
		}
		return false;
	}
	
	public UrlProfileDisplayInfo returnUrlProfile(Profile p){
		//if p equals null and there is no null profile in MDI this method will fail. Response will be null
		for(UrlProfileDisplayInfo url:this.getUrls()){
			if(p==null && url.getProfile()==null)
				return url;
			else if(p!=null && url.getProfile()!=null && 
					(p.getType()==url.getProfile().getType()))
				return url;
		}
		return null;
	}
	
	//getters and setters
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public Set<PropertyDisplayInfo> getProperties() {
		return properties;
	}

	public void setProperties(Set<PropertyDisplayInfo> properties) {
		this.properties = properties;
	}
	
	public Set<UrlProfileDisplayInfo> getUrls() {
		return urls;
	}

	public void setUrls(Set<UrlProfileDisplayInfo> urls) {
		this.urls = urls;
	}
}
