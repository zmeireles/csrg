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

import com.obc.csrg.events.ModelEventQueue;
import com.obc.csrg.constants.SearchObjectTypeEnum;

@Entity
@Name("searchObject")
@Scope(SESSION)
@Table(name = "search_object")
//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class SearchObject extends Model implements Serializable{

	@SequenceGenerator(name = "generator")
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "search_obj_id", nullable = false)
	private Long id;
	
	@Column(name = "obj_type")
	private SearchObjectTypeEnum objectType = SearchObjectTypeEnum.MODEL;
	
	@Column(name = "obj_name")
	private String objectName ="";
	
	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE,
			CascadeType.REMOVE }, fetch = FetchType.LAZY, mappedBy = "searchObject")
	//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
	private Set<SearchProperty> properties = new HashSet<SearchProperty>(0);
	
	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE,
			CascadeType.REMOVE }, fetch = FetchType.LAZY, mappedBy = "searchObject")
	//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
	private Set<KeywordsLang> keywords = new HashSet<KeywordsLang>(0);
	
	//constructors
	public SearchObject() {
	}

	//business logic

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
		if (object instanceof SearchObject) {
			final SearchObject obj = (SearchObject) object;
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

	public SearchObjectTypeEnum getObjectType() {
		return objectType;
	}

	public void setObjectType(SearchObjectTypeEnum objectType) {
		this.objectType = objectType;
	}

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	public Set<SearchProperty> getProperties() {
		return properties;
	}

	public void setProperties(Set<SearchProperty> properties) {
		this.properties = properties;
	}
	
	public Set<KeywordsLang> getKeywords() {
		return keywords;
	}

	public void setKeywords(Set<KeywordsLang> keywords) {
		this.keywords = keywords;
	}
}
