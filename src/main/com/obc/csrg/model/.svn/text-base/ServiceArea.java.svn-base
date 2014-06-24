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
@Name("serviceArea")
@Scope(SESSION)
@Table(name = "service_area")
//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class ServiceArea extends Model implements Serializable {

	@SequenceGenerator(name = "generator")
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "service_area_id", nullable = false)
	private Long id;

	@OneToMany(cascade = {CascadeType.MERGE,
			CascadeType.REMOVE }, fetch = FetchType.LAZY, mappedBy = "serviceArea")
	//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
	private Set<DepartmentArea> departments = new HashSet<DepartmentArea>(0);

	private String name = "";

	@OneToMany(cascade = { CascadeType.MERGE,
			CascadeType.REFRESH }, fetch = FetchType.LAZY, mappedBy = "serviceArea")
	//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
	private Set<User> users = new HashSet<User>(0);

	@OneToMany(cascade = { CascadeType.MERGE,
			CascadeType.REMOVE }, fetch = FetchType.LAZY, mappedBy = "serviceArea")
	//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
	private Set<ServiceAreaNotification> notifications = new HashSet<ServiceAreaNotification>(
			0);
	
	private String locale=Constants.DEFAULT_LOCALE;

	// constructors
	public ServiceArea() {
	}

	// business functions

	public int hashCode() {
		return (this.id == null) ? 0 : this.id.hashCode();
	}

	public boolean equals(Object object) {
		if (object instanceof ServiceArea) {
			final ServiceArea obj = (ServiceArea) object;
			return (this.id != null) ? this.id.equals(obj.id)
					: (obj.id == null);
		}
		return false;
	}

	@Override
	public List<User> relatedUsers(){
		List<User> users = new ArrayList<User>();
		for(User u:this.getUsers()){
			users.add(u);
		}
		return users;
	}
	public List<Department> relatedDepartments(){
		List<Department> departments = new ArrayList<Department>();
		for(DepartmentArea d:this.departments)
			departments.add(d.getDepartment());
		return departments;
	}
	@Override
	public boolean remove(EntityManager entityManager) {
		ModelEventQueue.fireOnBeforeModelRemoveEvent(this);
		// remove departments relation
		for (DepartmentArea d : this.departments) {
			d.getDepartment().getServiceAreas().remove(d);
			d.setDepartment(null);
			d.setServiceArea(null);
			entityManager.remove(d);
		}
		this.departments.clear();
		// remove users relation
		for (User u : this.users) {
			u.setServiceArea(null);
			entityManager.merge(u);
		}
		this.users.clear();
		//remove notifications relation
		for(ServiceAreaNotification n:this.notifications){
			n.getNotification().getServiceAreas().remove(n);
			n.setNotification(null);
			n.setServiceArea(null);
			entityManager.remove(n);
		}
		this.notifications.clear();
		
		entityManager.remove(this);
		ModelEventQueue.fireOnAfterModelRemoveEvent(this);
		return true;
	}

	@Override
	public String toString() {
		return this.name;
	}

	// getters and setters
	@Override
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public Set<DepartmentArea> getDepartments() {
		return departments;
	}

	public void setDepartments(Set<DepartmentArea> departments) {
		this.departments = departments;
	}
	
	public Set<ServiceAreaNotification> getNotifications() {
		return notifications;
	}

	public void setNotifications(Set<ServiceAreaNotification> notifications) {
		this.notifications = notifications;
	}
	
	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}
}
