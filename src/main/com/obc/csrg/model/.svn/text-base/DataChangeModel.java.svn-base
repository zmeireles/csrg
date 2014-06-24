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
@Name("dataChangeModel")
@Scope(SESSION)
@Table(name = "data_change_model")
//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class DataChangeModel extends Model implements Serializable{

	@SequenceGenerator(name = "generator")
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "data_change_model_id", nullable = false)
	private Long id;

	@Column(name="alias")
	private String alias=""; 
	
	@Column(name="name")
	private String name="";
	
	@Column(name="include_all_prop")
	private boolean includeAllProperties = false;
	
	@Column(name="notify_for_any_prop_change")
	private boolean notifyForAnyPropertyChange = false;
	
	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE,
			CascadeType.REMOVE }, fetch = FetchType.LAZY, mappedBy = "dataChangeModel")
	//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
	private Set<DataChangeProperty> properties = new HashSet<DataChangeProperty>(0);
	
	private String locale=Constants.DEFAULT_LOCALE;
	
	//constructors
	public DataChangeModel() {
	}

	//business logic

	@Override
	public String toString(){
		return this.name;
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
		if (object instanceof DataChangeModel) {
			final DataChangeModel obj = (DataChangeModel) object;
			return (this.id != null) ? this.id.equals(obj.id)
					: (obj.id == null);
		}
		return false;
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
	
	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isIncludeAllProperties() {
		return includeAllProperties;
	}

	public void setIncludeAllProperties(boolean includeAllProperties) {
		if(this.notifyForAnyPropertyChange)
			this.includeAllProperties=true;
		else
			this.includeAllProperties = includeAllProperties;
	}

	public boolean isNotifyForAnyPropertyChange() {
		return notifyForAnyPropertyChange;
	}

	public void setNotifyForAnyPropertyChange(boolean notifyForAnyPropertyChange) {
		this.notifyForAnyPropertyChange = notifyForAnyPropertyChange;
		if(this.notifyForAnyPropertyChange)
			this.setIncludeAllProperties(true);
	}
	
	public Set<DataChangeProperty> getProperties() {
		return properties;
	}

	public void setProperties(Set<DataChangeProperty> properties) {
		this.properties = properties;
	}
}
