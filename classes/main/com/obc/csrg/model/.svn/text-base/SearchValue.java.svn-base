package com.obc.csrg.model;

import static javax.persistence.GenerationType.SEQUENCE;
import static org.jboss.seam.ScopeType.SESSION;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

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
import org.hibernate.annotations.Index;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import com.obc.csrg.events.ModelEventQueue;

@Entity
@Name("searchValue")
@Scope(SESSION)
@Table(name = "search_value")
//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class SearchValue extends Model implements Serializable{

	@SequenceGenerator(name = "generator")
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "search_value_id", nullable = false)
	private Long id;
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "search_prop_id")
    private SearchProperty searchProperty;
	
	@OneToMany(cascade = {CascadeType.MERGE,
			CascadeType.REMOVE }, fetch = FetchType.LAZY, mappedBy = "searchValue")
	@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
	private Set<KeywordsLang> keywords = new HashSet<KeywordsLang>(0);

	@Index(name="obj_id_ix")
	@Column(name = "obj_id")
	private Long objectId;
	
	//constructors
	public SearchValue() {
	}

	//business logic

	public Object getModel(EntityManager entityManager){
		String modelName = this.getSearchProperty().getSearchObject().getObjectName();
		List models = entityManager.createQuery("select m from " + modelName + " m where m.id=?")
					.setParameter(1, this.getObjectId())
					.getResultList();
		if(models.size()>0)
			return models.get(0);
		return "";
	}
	@Override
	public boolean remove(EntityManager entityManager) {
		ModelEventQueue.fireOnBeforeModelRemoveEvent(this);
		
		if(this.searchProperty!=null){
			this.searchProperty.getValues().remove(this);
			this.searchProperty = null;
		}
		
		entityManager.remove(this);
		ModelEventQueue.fireOnAfterModelRemoveEvent(this);
		return true;
	}
	public int hashCode() {
		return (this.id == null) ? 0 : this.id.hashCode();
	}

	public boolean equals(Object object) {
		if (object instanceof SearchValue) {
			final SearchValue obj = (SearchValue) object;
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
	
	public SearchProperty getSearchProperty() {
		return searchProperty;
	}

	public void setSearchProperty(SearchProperty searchProperty) {
		this.searchProperty = searchProperty;
	}

	public Set<KeywordsLang> getKeywords() {
		return keywords;
	}

	public void setKeywords(Set<KeywordsLang> keywords) {
		this.keywords = keywords;
	}

	public Long getObjectId() {
		return objectId;
	}

	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}
}
