package com.obc.csrg.model;

import static javax.persistence.GenerationType.SEQUENCE;
import static org.jboss.seam.ScopeType.SESSION;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
@Name("serviceClassification")
@Scope(SESSION)
@Table(name = "service_classification")
//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class ServiceClassification extends Model implements Serializable {

	@SequenceGenerator(name = "generator")
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "service_classification_id", nullable = false)
	private Long id;

	private String name = "";

	@OneToMany(cascade = { CascadeType.MERGE,
			CascadeType.REFRESH }, fetch = FetchType.LAZY, mappedBy = "serviceClassification")
	//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
	private Set<Department> departments = new HashSet<Department>(0);
	
	@OneToMany(cascade = { CascadeType.MERGE,
			CascadeType.REMOVE }, fetch = FetchType.LAZY, mappedBy = "serviceClassification")
	//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
	private Set<ServiceClassificationNotification> notifications = new HashSet<ServiceClassificationNotification>(
			0);
	
	private String locale=Constants.DEFAULT_LOCALE;
	
	// constructors

	public ServiceClassification() {
	}

	// business functions
	@Override
	public String toString() {
		return this.name;
	}
	
	public List<Department> relatedDepartments(){
		List<Department> departments = new ArrayList<Department>();
		for(Department d:this.departments)
			departments.add(d);
		return departments;
	}
	
	@Override
	public List<User> relatedUsers(){
		List<User> users = new ArrayList<User>();
		for(Department d:departments){
			users.addAll(d.relatedUsers());
		}
		return users;
	}
	
	public List<User> relatedUsers(EntityManager entityManager){
		List<User> users = new ArrayList<User>();
		for(Department d:departments){
			List<User> items = entityManager.createQuery(
					"select m.user from UserDepartment m where m.department.id is not null and m.department.id=?")
					.setParameter(1, d.getId())
					.getResultList();
			users.addAll(items);
		}
		return users;
	}
	
	@Override
	public boolean remove(EntityManager entityManager) {
		ModelEventQueue.fireOnBeforeModelRemoveEvent(this);
		//detach from departments
		for(Department d:this.departments){
			d.setServiceClassification(null);
			entityManager.merge(d);
		}
		this.departments.clear();
		//remove notifications relation
		for(ServiceClassificationNotification n:this.notifications){
			n.getNotification().getServiceClassifications().remove(n);
			n.setNotification(null);
			n.setServiceClassification(null);
			entityManager.remove(n);
		}
		this.notifications.clear();
		
		entityManager.remove(this);
		ModelEventQueue.fireOnAfterModelRemoveEvent(this);
		return true;
	}
	public int hashCode() {
		return (this.id == null) ? 0 : this.id.hashCode();
	}

	public boolean equals(Object object) {
		if (object instanceof ServiceClassification) {
			final ServiceClassification obj = (ServiceClassification) object;
			return (this.id != null) ? this.id.equals(obj.id)
					: (obj.id == null);
		}
		return false;
	}

	// getters and setters
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Set<Department> getDepartments() {
		return departments;
	}

	public void setDepartments(Set<Department> departments) {
		this.departments = departments;
	}
	
	public Set<ServiceClassificationNotification> getNotifications() {
		return notifications;
	}

	public void setNotifications(
			Set<ServiceClassificationNotification> notifications) {
		this.notifications = notifications;
	}
	
	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}
}
