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
@Name("profCategory")
@Scope(SESSION)
@Table(name = "prof_category")
//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
public class ProfCategory extends Model implements Serializable {

	@SequenceGenerator(name = "generator")
	@Id
	@GeneratedValue(strategy = SEQUENCE, generator = "generator")
	@Column(name = "prof_categ_id", nullable = false)
	private Long id;

	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE,
			CascadeType.REFRESH }, fetch = FetchType.LAZY, mappedBy = "profCategory")
	//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
	private Set<User> users = new HashSet<User>(0);

	private String name = "";

	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE,
			CascadeType.REMOVE }, fetch = FetchType.LAZY, mappedBy = "profCategory")
	//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
	private Set<ProfCategoryNotification> notifications = new HashSet<ProfCategoryNotification>(
			0);

	private String locale=Constants.DEFAULT_LOCALE;
	
	// constructors
	public ProfCategory() {
	}

	// business functions

	public int hashCode() {
		return (this.id == null) ? 0 : this.id.hashCode();
	}

	public boolean equals(Object object) {
		if (object instanceof ProfCategory) {
			final ProfCategory obj = (ProfCategory) object;
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
	@Override
	public boolean remove(EntityManager entityManager) {
		ModelEventQueue.fireOnBeforeModelRemoveEvent(this);
		//remove users relation
		for (User u : this.users) {
			u.setProfCategory(null);
			entityManager.merge(u);
		}
		this.users.clear();
		// remove notification relation
		for (ProfCategoryNotification n : this.notifications) {
			n.getNotification().getProfCategories().remove(n);
			n.setNotification(null);
			n.setProfCategory(null);
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

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<ProfCategoryNotification> getNotifications() {
		return notifications;
	}

	public void setNotifications(Set<ProfCategoryNotification> notifications) {
		this.notifications = notifications;
	}
	
	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}
}
