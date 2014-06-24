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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import com.obc.csrg.constants.Constants;
import com.obc.csrg.events.ModelEventQueue;

@Entity
@Name("dataChangeProperty")
@Scope(SESSION)
@Table(name = "data_change_prop")
//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class DataChangeProperty extends Model implements Serializable{

	@SequenceGenerator(name = "generator")
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "data_change_prop_id", nullable = false)
	private Long id;

	@Column(name = "data_type")
	private String dataType;
	
	@Column(name="name")
	private String name; //name that can be changed by user
	
	@Column(name="alias")
	private String alias=""; //original name
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "data_change_model_id")
	private DataChangeModel dataChangeModel;
	
	@Column(name="notify_on_change")
	private boolean notifyOnChange=false;
	
	@Column(name="include")
	private boolean include=false;
	
	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE,
			CascadeType.REMOVE }, fetch = FetchType.LAZY, mappedBy = "dataChangeProperty")
	//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
	private Set<DataChangeValue> values = new HashSet<DataChangeValue>(0);
	
	private String locale=Constants.DEFAULT_LOCALE;
	
	//constructors
	public DataChangeProperty() {
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
		if (object instanceof DataChangeProperty) {
			final DataChangeProperty obj = (DataChangeProperty) object;
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
	
	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
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

	public DataChangeModel getDataChangeModel() {
		return dataChangeModel;
	}

	public void setDataChangeModel(DataChangeModel dataChangeModel) {
		this.dataChangeModel = dataChangeModel;
	}

	public boolean isNotifyOnChange() {
		return notifyOnChange;
	}

	public void setNotifyOnChange(boolean notifyOnChange) {
		this.notifyOnChange = notifyOnChange;
		if(this.notifyOnChange)
			this.include = true;
	}
	
	public boolean isInclude() {
		return include;
	}

	public void setInclude(boolean include) {
		if(this.notifyOnChange)
			this.include = true;
		else
			this.include = include;
	}
	
	public Set<DataChangeValue> getValues() {
		return values;
	}

	public void setValues(Set<DataChangeValue> values) {
		this.values = values;
	}
}
